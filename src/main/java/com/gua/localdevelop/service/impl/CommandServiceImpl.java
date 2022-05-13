package com.gua.localdevelop.service.impl;

import com.gua.localdevelop.service.CommandService;
import com.gua.localdevelop.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static com.gua.localdevelop.contants.ErrorCode.SHELL_EXCEPTION;

@Slf4j
@Service
public class CommandServiceImpl implements CommandService, InitializingBean {

    private String threadName = "command-service";

    private Integer taskQueueMaxStorage = 20;

    private Integer corePoolSize = 4;

    private Integer maximumPoolSize = 8;

    private Integer keepAliveSeconds = 15;
    private ThreadPoolExecutor executor;
    private static final String BASH = "sh";
    private static final String BASH_PARAM = "-c";


    // use thread pool to read streams
    @Override
    public void afterPropertiesSet() {
        executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveSeconds, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(taskQueueMaxStorage),
                new ThreadFactory() {
                    public Thread newThread(Runnable r) {
                        return new Thread(r, threadName + r.hashCode());
                    }
                },
                new ThreadPoolExecutor.AbortPolicy());
    }



    @Override
    public ResultVO executeCmd(String cmd, String... args) {
        Process p = null;
        String res;
        log.info("CommandService cmd info : {}", cmd);
        ResultVO result;
        try {
            // need to pass command as bash's param,
            // so that we can compatible with commands: "echo a >> b.txt" or "bash a && bash b"
            List<String> cmds = new ArrayList<>();
            cmds.add(BASH);
            cmds.add(cmd);
            for (String arg : args) {
                cmds.add(arg);
            }
            ProcessBuilder pb = new ProcessBuilder(cmds);
            log.info("CommandService cmds info : {}", cmds);
            p = pb.start();
            Future<String> errorFuture = executor.submit(new ReadTask(p.getErrorStream()));
            Future<String> resFuture = executor.submit(new ReadTask(p.getInputStream()));
            int exitValue = p.waitFor();
            if (exitValue > 0) {
                log.info("exec cmd error: {} ", errorFuture.get());
                res = errorFuture.get();
                result = ResultVO.error(SHELL_EXCEPTION, res);
                //throw new RuntimeException(errorFuture.get());
            } else {
                res = resFuture.get();
                if(!res.equals("success")){
                    result = ResultVO.error(SHELL_EXCEPTION, res);
                }else {
                    result = ResultVO.success(res);
                }
            }
        } catch (Exception e) {
            log.error("exec cmd error: {} ", e);
            res = e.getMessage();
            result = ResultVO.error(SHELL_EXCEPTION, res);
            //throw new RuntimeException(e);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        // remove System.lineSeparator() (actually it's '\n') in the end of res if exists
        return result;
    }

    @Override
    public ResultVO executeServer(String shell, String serverUserName, String serverUserPassWord, String localHost, String pwd, String exec, String host) throws UnknownHostException {
        return executeCmd(shell, serverUserName, serverUserPassWord, localHost, pwd, exec, host);
    }

    @Override
    public ResultVO executePing(String shell, String serverHost, String targetServer) {
//        shell = shell + " " + serverHost + " " + targetServer;
        return executeCmd(shell, serverHost, targetServer);
    }

    class ReadTask implements Callable<String> {
        InputStream is;

        ReadTask(InputStream is) {
            this.is = is;
        }

        @Override
        public String call() {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
    }





}
