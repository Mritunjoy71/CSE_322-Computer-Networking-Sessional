/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smtpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rakibul
 */
public class SMTPClient {

    public Scanner userInput;
    static final int port = 25;
    static String EmailSubject = "";
    static String EmailBody = "";
    static BufferedReader br = null;

    public String getMessage() {
        String hhh = "";
        try {
            hhh = br.readLine();
        } catch (IOException ex) {
            Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hhh;
    }

    public static void main(String[] args) {
        int STATE = 1;
        int messageDeliveryConfirmed = 0;
        Scanner userInput = new Scanner(System.in);

        String senderEmailAddr = null;
        String receiverEmailAddr = null;

        PrintWriter pr = null;
        Socket ClientSocket = null;

        int currentClient = 0;

        //System.out.println("No.of clients?");
        //int noOfClients = Integer.parseInt(userInput.nextLine());
        while (true) {
            System.out.println("Do you want to send Mail?? Y/N .N to exit.Y to send email.::");
            String reply1 = "";
            reply1 = userInput.nextLine();
            System.out.println("");

            if (reply1.equalsIgnoreCase("Y")) {
                try {
                    /*String SMTPServer ="webmail.buet.ac.bd";
                                InetAddress serverDetails = InetAddress.getByName(SMTPServer);*/
                    InetAddress serverDetails = InetAddress.getLocalHost();
                    String ServerIP = serverDetails.getHostAddress();
                    ClientSocket = new Socket(ServerIP, port);
                    br = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
                    pr = new PrintWriter(ClientSocket.getOutputStream());

                    String isConnected = br.readLine();
                    System.out.println(isConnected);

                    if (isConnected.startsWith("220")) {
                        System.out.println("Connected to the Server..");
                        pr.println("HELO " + serverDetails.getHostName());
                        pr.flush();

                        String serverReply = "";
                        
                        
                        
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                                Future future = null;

                                Callable callable = new Callable() {
                                    @Override
                                    public String call() throws Exception {
                                        return new SMTPClient().getMessage();
                                        //To change body of generated methods, choose Tools | Templates.
                                    }

                                };
                                //String serverReply ="";
                                future = executor.submit(callable);
                                try {
                                   serverReply= future.get(20, TimeUnit.SECONDS).toString();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ExecutionException ex) {
                                    Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (TimeoutException ex) {
                                    System.out.println("timeout ");
                                    //Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                finally{
                                    executor.shutdown();
                                }
                        
                        
                        
                        
                        System.out.println(serverReply);

                        if (serverReply.startsWith("250")) {
                            System.out.println("Ready to deliver Email.expexting MAIL command.");
                            STATE = 1;
                        } else if (serverReply.startsWith("5")) {
                            System.out.println("Error!! Try again.(HELO) command.");
                        }

                    } else if (isConnected.startsWith("554")) {
                        System.out.println("Server Address Not Valid.");
                    }
                } catch (UnknownHostException ex) {
                    //Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error!!!server is not Identified.");
                } catch (IOException ex) {
                    System.out.println("Server did not respond!!");
                    // Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            } else if (reply1.equalsIgnoreCase("N")) {
                System.exit(1);
            } else {
                System.out.println("Input is not valid.Enter Y/N or y/n.");
            }
            String reply = "Y";
            //System.out.println("Do you want to send Mail again?? Y/N .N to quit.Y to send email.::");

            //reply = userInput.nextLine();
            //System.out.println("");
            if (reply.equalsIgnoreCase("Y")) {

                while (true) {

                    switch (STATE) {

                        case 1:
                            System.out.println("Enter sender Email Address:::  ");
                            senderEmailAddr = userInput.nextLine();
                            try {

                                pr.println("MAIL FROM:<" + senderEmailAddr + ">");//problem
                                pr.flush();
                                //final Duration timeout = Duration.ofSeconds(30);
                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                Future future = null;

                                Callable callable = new Callable() {
                                    @Override
                                    public String call() throws Exception {
                                        return new SMTPClient().getMessage();
                                        //To change body of generated methods, choose Tools | Templates.
                                    }

                                };
                                String serverReply = "";
                                future = executor.submit(callable);
                                try {
                                    serverReply = future.get(20, TimeUnit.SECONDS).toString();
                                } catch (InterruptedException ex) {
                                    Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ExecutionException ex) {
                                    Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (TimeoutException ex) {
                                    System.out.println("timeout ");
                                    //Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                } finally {
                                    executor.shutdown();
                                }

                                System.out.println(serverReply);

                                if (serverReply.startsWith("250")) {
                                    STATE = 2;
                                } else {
                                    //System.out.println("Problem with client no." + (currentClient + 1));
                                    System.out.println("Error::" + serverReply + ".Try again.");
                                }
                                System.out.println("Do you Want to quit?Y/N");
                                String st2 = userInput.nextLine();
                                if (st2.equalsIgnoreCase("Y")) {
                                    messageDeliveryConfirmed = 1;
                                    pr.println("RSET");
                                    pr.flush();
                                    System.out.println(br.readLine());
                                }
                            } catch (IOException ex) {
                                //Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                System.out.println("Server did not respond.Try again?Y/N::");
                                String st = userInput.nextLine();
                                if (st.equalsIgnoreCase("N")) {
                                    pr.println("QUIT");
                                    pr.flush();
                                }
                            }

                            break;
                        case 2:
                            try {
                                System.out.println("Enter no. of receivers::");
                                int noOfClients = Integer.parseInt(userInput.nextLine());
                                System.out.println("Enter receiver email address:::  ");
                                int flag = 1;
                                for (int i = 0; i < noOfClients; i++) {
                                    receiverEmailAddr = userInput.nextLine();
                                    pr.println("RCPT TO:<" + receiverEmailAddr + ">");
                                    pr.flush();
                                    String serverReply = "";

                                    ExecutorService executor = Executors.newSingleThreadExecutor();
                                    Future future = null;

                                    Callable callable = new Callable() {
                                        @Override
                                        public String call() throws Exception {
                                            return new SMTPClient().getMessage();
                                            //To change body of generated methods, choose Tools | Templates.
                                        }

                                    };
                                    //String serverReply ="";
                                    future = executor.submit(callable);
                                    try {
                                        serverReply = future.get(20, TimeUnit.SECONDS).toString();
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ExecutionException ex) {
                                        Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (TimeoutException ex) {
                                        System.out.println("timeout ");
                                        //Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                    } finally {
                                        executor.shutdown();
                                    }

                                    System.out.println(serverReply);
                                    if (serverReply.startsWith("25")) {
                                        //STATE = 3;
                                        //break;
                                    } else {
                                        //flag=0;
                                        i--;
                                        System.out.println("receiver email address not recognized.try again.");
                                        break;
                                    }
                                }
                                if (flag == 1) {
                                    STATE = 3;
                                } else {
                                    System.out.println("start again");
                                }
                                System.out.println("Do you Want to quit?Y/N");
                                String st2 = userInput.nextLine();
                                if (st2.equalsIgnoreCase("Y")) {
                                    messageDeliveryConfirmed = 1;
                                    pr.println("RSET");
                                    pr.flush();
                                    System.out.println(br.readLine());
                                }

                            } catch (Exception ex) {
                                Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;
                        case 3:

                            //System.out.println("Do you want to change receiver email address?Y/N");
                            String st = "N";

                            if (st.equalsIgnoreCase("N")) {

                                System.out.println("Are you ready to send email?Y/N");
                                String serverReply = userInput.nextLine();

                                if (serverReply.equalsIgnoreCase("Y")) {
                                    pr.println("DATA"); //Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                    pr.flush();
                                    String serverReplydata ="";
                                    
                                    
                                    
                                    ExecutorService executor = Executors.newSingleThreadExecutor();
                                    Future future = null;
                                    Callable callable = new Callable() {
                                        @Override
                                        public String call() throws Exception {
                                            return new SMTPClient().getMessage();
                                            //To change body of generated methods, choose Tools | Templates.
                                        }
                                        
                                    };
                                    //String serverReply ="";
                                    future = executor.submit(callable);
                                    try {
                                        serverReplydata= future.get(20, TimeUnit.SECONDS).toString();
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (ExecutionException ex) {
                                        Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (TimeoutException ex) {
                                        System.out.println("timeout ");
                                        //Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    finally{
                                        executor.shutdown();
                                    }
                                    
                                    
                                    
                                    System.out.println(serverReplydata);
                                    if (serverReplydata.startsWith("354")) {
                                        STATE = 4;
                                    } else {
                                        System.out.println(serverReplydata + ".Email can not be sent.");
                                    }
                                }
                            }
                            break;
                        case 4:

                            System.out.println("Compose Email-->");
                            System.out.println("Subject::");
                            EmailSubject = "Subject:" + userInput.nextLine() + "\n"
                                    + "FROM:" + senderEmailAddr + "\n"
                                    + "TO:" + receiverEmailAddr + "\n\n";
                            System.out.println("Message::");
                            String TEMPEmailBody = userInput.nextLine();
                            //EmailBody=TEMPEmailBody;
                            EmailBody="";
                            while (true) {
                                if (!TEMPEmailBody.trim().equals(".")) {
                                    EmailBody += TEMPEmailBody;
                                    TEMPEmailBody = userInput.nextLine();

                                } else {
                                    break;
                                }
                            }

                            pr.println(EmailSubject + EmailBody + "\r\n.\r\n");
                            pr.flush();

                            //String serverReply = br.readLine();
                            
                            
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            Future future = null;
                            Callable callable = new Callable() {
                                @Override
                                public String call() throws Exception {
                                    return new SMTPClient().getMessage();
                                    //To change body of generated methods, choose Tools | Templates.
                                }
                                
                            };
                            String serverReply ="";
                            future = executor.submit(callable);
                            try {
                                serverReply= future.get(20, TimeUnit.SECONDS).toString();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ExecutionException ex) {
                                Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (TimeoutException ex) {
                                System.out.println("timeout ");
                                //Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            finally{
                                executor.shutdown();
                            }
                            System.out.println(serverReply);
                            if (serverReply.startsWith("250")) {
                                messageDeliveryConfirmed = 1;
                                //pr.println("QUIT");
                                //pr.flush();
                            }
                            break;
                    }

                    if (messageDeliveryConfirmed == 1) {
                        messageDeliveryConfirmed = 0;
                        pr.println("QUIT");
                        pr.flush();
                        currentClient++;
                        /*try {
                            System.out.println(br.readLine());
                        } catch (IOException ex) {
                            Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
                        }*/
                        //STATE = 1;
                        //currentClient++;
                        break;
                    }

                }
            }
            System.out.println("Do you want to send Mail again?? Y/N .N to quit.Y to send email.::");
            //String reply = "Y";
            reply = userInput.nextLine();
            System.out.println("");
            if (reply.equalsIgnoreCase("Y")) {
                STATE = 1;
            } else if (reply1.equalsIgnoreCase("N")) {
                // pr.println("QUIT");
                //pr.flush();
                break;
            }
        }
        try {
            br.close();
            pr.close();
            ClientSocket.close();
            userInput.close();
        } catch (IOException ex) {
            Logger.getLogger(SMTPClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
