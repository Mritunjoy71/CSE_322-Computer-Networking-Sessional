/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http_server;

import static com.sun.xml.internal.ws.model.RuntimeModeler.PORT;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rakibul
 */
public class HTTP_Server implements Runnable {

    static int clientCount = 0;
    static final int PORT = 6789;
    static ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    public HTTP_Server(Socket c) {
        clientSocket = c;
    }

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("server started.listening on port 6789.");
            while (true) {
                HTTP_Server perClient = new HTTP_Server(serverSocket.accept());
                clientCount++;
                //System.out.println("Client " + clientCount + " connected.");
                Thread perClientThread = new Thread(perClient);
                perClientThread.start();

            }
        } catch (IOException ex) {
            System.out.println("Server did not respond." + ex.getMessage());
        }
    }

    @Override
    public void run() {
        PrintWriter pw = null;
        BufferedReader br = null;
        String requestedFileName = " ";
        String firstToken = " ";
        BufferedOutputStream bos = null;
        String requestedFileMIMEtype = null;
        try {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(clientSocket.getOutputStream());
            bos = new BufferedOutputStream(clientSocket.getOutputStream());
            String input = " ";
            input = br.readLine();
            if(input!=null){
            System.out.println("client::"+input+" time:: "+new Date());
            StringTokenizer stringTokenizer = new StringTokenizer(input, " /");
            if (stringTokenizer.hasMoreTokens()) {
                firstToken = stringTokenizer.nextToken();
            }
            if (firstToken.equals("GET")) {
                requestedFileName = stringTokenizer.nextToken();
                if (requestedFileName.equals("HTTP")) {
                    requestedFileName = "index.html";
                }
                //System.out.println(requestedFileName);
                StringTokenizer requestedFileNameTokenizer = new StringTokenizer(requestedFileName, ".");
                String requestedFileAbsoluteName = requestedFileNameTokenizer.nextToken();
                String requestedFileNameExtension = requestedFileNameTokenizer.nextToken();
               // System.out.println(requestedFileNameExtension);
                if (requestedFileNameExtension.equals("html")) {
                    requestedFileMIMEtype = "text/html";
                } else if (requestedFileNameExtension.equals("jpg") || requestedFileNameExtension.equals("jpeg")) {
                    requestedFileMIMEtype = "image/jpeg";
                } else if (requestedFileNameExtension.equals("gif")) {
                    requestedFileMIMEtype = "image/gif";
                } else if (requestedFileNameExtension.equals("txt")) {
                    requestedFileMIMEtype = "text/plain";
                } else if (requestedFileNameExtension.equals("pdf")) {
                    requestedFileMIMEtype = "application/pdf";
                } else if (requestedFileNameExtension.equals("png")) {
                    requestedFileMIMEtype = "image/png";
                } else if (requestedFileNameExtension.equals("ico")) {
                    requestedFileMIMEtype = "image/x-icon";
                }
                File requestedFile = new File("D:\\3-2 files\\networking lab\\HTTP_Server\\" + requestedFileName);
                if (requestedFile.isFile()) {
                    int requestedFileLength = (int) requestedFile.length();
                    //System.out.println("file found");
                    byte[] requestedFileData = new byte[requestedFileLength];
                    FileInputStream fis = new FileInputStream(requestedFile);
                    fis.read(requestedFileData);
                    System.out.println("server :: 200 OK time:: "+new Date());
                         
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Date: " + new Date());
                    pw.println("Content-type: " + requestedFileMIMEtype);
                    pw.println("Content-length: " + requestedFileLength);
                    pw.println();
                    pw.flush();

                    bos.write(requestedFileData, 0, requestedFileLength);
                    bos.flush();
                } else {
                    //System.out.println("server replied with ::");
                    requestedFileName = "404_not_found.html";
                    File requestedFileError = new File("D:\\3-2 files\\networking lab\\HTTP_Server\\" + requestedFileName);
                    int requestedFileErrorLength = (int) requestedFileError.length();
                    //System.out.println("file found");
                    byte[] requestedFileErrorData = new byte[requestedFileErrorLength];
                    FileInputStream fis = new FileInputStream(requestedFileError);
                    fis.read(requestedFileErrorData);
                    System.out.println("server::404 Not Found\n date:: "+new Date());
                    pw.println("HTTP/1.1 404 Not Found");
                    pw.println("Date: " + new Date());
                    pw.println("Content-type: text/html");
                    pw.println("Content-length: " + requestedFileErrorLength);
                    pw.println();
                    pw.flush();

                    bos.write(requestedFileErrorData, 0, requestedFileErrorLength);
                    bos.flush();
                }

            } else if (firstToken.equals("POST")) {
                StringBuilder payload = new StringBuilder();
                while (br.ready()) {
                    payload.append((char) br.read());
                }
                //System.out.println("Payload data is: " + payload.toString());
                String value = "";
                String referer = "";
                Scanner scanner = new Scanner(payload.toString());
                while (scanner.hasNextLine()) {

                    value = scanner.nextLine();
                    if (value.startsWith("Referer")) {
                        referer = value;
                    }
                    // process the line
                }
                scanner.close();
                //System.out.println(referer);
                String refererName = "";
                StringTokenizer st = new StringTokenizer(referer, "/");
                while (st.hasMoreTokens()) {
                    refererName = st.nextToken();
                }
                //System.out.println(refererName);
                System.out.println("From client: "+value);
                StringBuilder contentBuilder = new StringBuilder();
                try {
                    BufferedReader in = new BufferedReader(new FileReader("D:\\3-2 files\\networking lab\\HTTP_Server\\" + refererName));
                    String str;
                    while ((str = in.readLine()) != null) {
                        contentBuilder.append(str+"\n");
                    }
                    in.close();
                } catch (IOException e) {
                }
                String contentOfPage = contentBuilder.toString();
                //System.out.println(contentOfPage);
                StringTokenizer tok=new StringTokenizer(contentOfPage," ");
                String Action="";
                while(tok.hasMoreTokens()){
                    //i++;
                    String a=tok.nextToken();
                    if(a.startsWith("action")){
                        Action=a;
                        break;
                    }
                }
                contentOfPage=contentOfPage.replace(Action,"action=\"http://localhost:6789/"+refererName+"\"");
                //System.out.println(contentOfPage);
                StringTokenizer toki=new StringTokenizer(contentOfPage," ");
                Scanner scanneri = new Scanner(contentOfPage);
                String data="";
                
                while (scanneri.hasNextLine()) {

                    String valuei = scanneri.nextLine();
                    
                    if (valuei.startsWith("<form")) {
                        data+="<p> Post Data::"+value+"</p>\n"+valuei;
                    }
                    else{
                        data+=valuei+"\n";
                    }
                    // process the line
                }
                scanneri.close();
                //System.out.println(data);
                
                byte[] postdata = data.getBytes();
                    //FileInputStream fis = new FileInputStream(requestedFileError);
                    //fis.read(requestedFileErrorData);
                    System.out.println("server::200 OK time:: "+new Date());
                    pw.println("HTTP/1.1 200 OK");
                    pw.println("Date: " + new Date());
                    pw.println("Content-type: text/html");
                    pw.println("Content-length: " + postdata.length);
                    pw.println();
                    pw.flush();

                    bos.write(postdata, 0, postdata.length);
                    bos.flush();

            }
            }
        } catch (IOException ex) {
            Logger.getLogger(HTTP_Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
            try {
                br.close();
                bos.close();
            } catch (IOException ex) {
                Logger.getLogger(HTTP_Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
