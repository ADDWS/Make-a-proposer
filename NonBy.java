//import javax.swing.*;
//import java.io.IOException;
//import java.lang.ref.Cleaner;
//
//public class NonBy {
//
//    protected static final String localhost = "0.0.0.0";
//    protected static final long CURRENT_TIME_LEADING_NUM = 1602015000000L;
//    protected static final String candidates[] = {"M1","M2","M3"};
//    protected static final int[] portSet = {2181,2182,2183,2184,2185,2186,2187,2188,2189};
//
//
//    protected static Proposer M1;
//    protected static Proposer M2;
//    protected static Proposer M3;
//    protected static Proposer M4;
//    protected static Proposer M5;
//    protected static Proposer M6;
//    protected static Proposer M7;
//    protected static Proposer M8;
//    protected static Proposer M9;
//
//    protected static final int M1Port = 2181;
//    protected static final int M2Port = 2182;
//    protected static final int M3Port = 2183;
//    protected static final int M4Port = 2184;
//    protected static final int M5Port = 2185;
//    protected static final int M6Port = 2186;
//    protected static final int M7Port = 2187;
//    protected static final int M8Port = 2188;
//    protected static final int M9Port = 2189;
//
//    protected static void initialProposers(){
//        System.out.println("Initialization:");
//        M1 = new Proposer(M1Port,candidates[0]);
//        M2 = new Proposer(M2Port,candidates[1]);
//        M3 = new Proposer(M3Port,candidates[2]);
//        M4 = new Proposer(M4Port,candidates[(int)(Math.random()*3)]);
//        M5 = new Proposer(M5Port,candidates[(int)(Math.random()*3)]);
//        M6 = new Proposer(M6Port,candidates[(int)(Math.random()*3)]);
//        M7 = new Proposer(M7Port,candidates[(int)(Math.random()*3)]);
//        M8 = new Proposer(M8Port,candidates[(int)(Math.random()*3)]);
//        M9 = new Proposer(M9Port,candidates[(int)(Math.random()*3)]);
//    }
//
//    protected static void startAccepter(){
//
//        new Thread(()-> {
//            try {
//                M1.startAcceptor(M1Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//        new Thread(()-> {
//            try {
//                M2.startAcceptor(M2Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//        new Thread(()-> {
//            try {
//                M3.startAcceptor(M3Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//        new Thread(()-> {
//            try {
//                M4.startAcceptor(M4Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//        new Thread(()-> {
//            try {
//                M5.startAcceptor(M5Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//        new Thread(()-> {
//            try {
//                M6.startAcceptor(M6Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//        new Thread(()-> {
//            try {
//                M7.startAcceptor(M7Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//        new Thread(()-> {
//            try {
//                M8.startAcceptor(M8Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//        new Thread(()-> {
//            try {
//                M9.startAcceptor(M9Port);
//            } catch (Exception e) {
//                System.out.println("close the socket");
//            }
//        }).start();
//
//    }
//
//    protected static void prepare(Proposer proposer, long pidPrefix){
//        for(int i=0; i< portSet.length; i++){
//            int finalI = i;
//            if(proposer.getPort() == portSet[finalI]){
//                System.out.println(("In phase 1: " + portSet[i] + " prepare for voting " + proposer.getVotingName()));
//                continue;
//            }
//
//            new Thread(){
//                @Override
//                public synchronized  void  run(){
//                    try{
//                        proposer.prepare(localhost, portSet[finalI],pidPrefix);
//                    } catch (Exception e){
//                        System.out.println("lost connection");
//                    }
//                }
//            }.start();
//        }
//    }
//
//    protected static void commit(Proposer proposer, String candidate){
//        for(int i =0; i<portSet.length; i++){
//            int finalI = i;
//            if(proposer.getPort() == portSet[finalI])
//                continue;
//
//            new Thread(){
//                @Override
//                public synchronized void  run(){
//                    try {
//                        proposer.commit(localhost, portSet[finalI], candidate);
//                    }catch (IOException | InterruptedException e){
//                        System.out.println("lost connection");
//                    }
//                }
//            }.start();
//        }
//    }
//
//
//    protected  void learn(Proposer proposer){
//        for(int i =0; i<portSet.length;i++){
//            int finalI = i;
//            if(proposer.getPort() == portSet[finalI])
//                continue;
//
//            new Thread(){
//                @Override
//                public synchronized void run(){
//                    try{
//                        proposer.tellToLearn(localhost,portSet[finalI]);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        System.out.println("lost connection");
//                    }
//                }
//            }.start();
//        }
//    }
//
//    protected void voting(Proposer councilor){
//        new Thread(()->{
//        long pidPrefix = System.currentTimeMillis() - CURRENT_TIME_LEADING_NUM;
//        while (true){
//            try{
//                prepare(councilor,pidPrefix);
//                Thread.sleep((long)(Math.random()*6000));
//
//                if(councilor.getAck_num() > portSet.length/2){
//                    System.out.println("M" + (councilor.getPort()-2180) + "has more than half promise");
//                    commit(councilor, councilor.getVotingName());
//                    Thread.sleep((long) (Math.random()*3000));
//                    if(councilor.getAcceptNum() > portSet.length/2){
//                        System.out.println("learn stage:");
//                        learn(councilor);
//                    }
//                    break;
//                }else {
//                    pidPrefix = councilor.getPid()/1000 + 1;
//                }
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//        }).start();
//    }
//
//    protected void stop() throws IOException{
//        M1.end();
//        M2.end();
//        M3.end();
//        M4.end();
//        M5.end();
//        M6.end();
//        M7.end();
//        M8.end();
//        M9.end();
//    }
//
//
//}
