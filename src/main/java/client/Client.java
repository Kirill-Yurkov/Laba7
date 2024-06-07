package client;

import client.managers.CommandInvoker;
import client.managers.InputOutput;
import client.managers.TCPClient;
import client.utilities.TicketCreator;
import commons.exceptions.BadResponseException;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.exceptions.ServerMainResponseException;
import commons.requests.RequestAuth;
import lombok.Getter;
import lombok.Setter;

import java.io.*;

@Getter
public class Client {
    private final CommandInvoker commandInvoker = new CommandInvoker(this);
    private final TicketCreator ticketCreator = new TicketCreator(this);
    private final InputOutput inputOutput = new InputOutput();
    private final TCPClient tcpClient = new TCPClient(this);
    private boolean clientOn;
    private String login;
    private String password;
    @Setter
    private boolean withFile = false;

    public Client(BufferedReader readerConsole, BufferedOutputStream writer ) {
        inputOutput.setSecondReaderConsole(readerConsole);
        inputOutput.setReaderConsole(readerConsole);
        inputOutput.setWriter(writer);
    }


    public static void main(String[] args) {
        Client client = new Client(new BufferedReader(new InputStreamReader(System.in)), new BufferedOutputStream(System.out));
        client.start();
    }

    public void stop() {
        clientOn = false;
        tcpClient.closeConnection();
        try {
            inputOutput.getReaderConsole().close();
            inputOutput.getWriter().close();
        } catch (IOException ignored) {}
        System.exit(0);
    }

    private void initialize(){
        clientOn = true;
        if(!tcpClient.openConnection()){
            return;
        }
        do{
            inputOutput.outPut("Enter your username: \n~ ");
            login = inputOutput.inPutConsole().strip();
            inputOutput.outPut("Enter your password: \n~ ");
            password = inputOutput.inPutConsole().strip();
        }while (!auth(login, password));
    }
    private boolean auth(String login, String password){
        try {
            if (login.isBlank() || password.isBlank()){
                inputOutput.outPut("Remove the empty lines from the auth \n");
                return false;
            }
            tcpClient.getAnswer(new RequestAuth(login, password));
            inputOutput.outPut(tcpClient.getAnswer(new RequestAuth(login, password)) + " \n");
            return true;
        } catch (BadResponseException e) {
            inputOutput.outPut("Problem: " + e.getMessage() + " \n");
            return false;
        }
    }
    public void start() {
        boolean isCommandWas = true;
        initialize();
        while (clientOn) {
            try {
                if (isCommandWas){
                    inputOutput.outPut("Enter the command (use the 'help' command for help) \n~ ");
                } else {
                    inputOutput.outPut("~ ");
                }
                String commandFromConsole = inputOutput.inPutConsole();
                if (commandFromConsole == null){
                    inputOutput.outPut("\nA shutdown signal has been received.");
                    stop();
                    return;
                }
                if(commandFromConsole.isBlank() || commandFromConsole.isEmpty()){
                    isCommandWas = false;
                } else {
                    isCommandWas = true;
                    String str = invoke(commandFromConsole);
                    if (str != null) {
                        inputOutput.outPut("\n"+str + "\n");
                        inputOutput.outPut("\n");
                    } else {
                        inputOutput.outPut("\n");
                    }
                }
            } catch (ServerMainResponseException e) {
                inputOutput.outPut("Command isn't valid: " + e.getMessage() + "\n");
                inputOutput.outPut("\n");
            }
        }
    }

    public void start(File file) {
        try {
            FileReader f = new FileReader(file.getAbsolutePath());
            BufferedReader br = new BufferedReader(f);
            inputOutput.setReaderConsole(br);
            String commandFromConsole;
            while ((commandFromConsole = br.readLine()) != null) {
                try {
                    String str = invoke(commandFromConsole);
                    if (str != null) {
                        inputOutput.outPut(str + "\n");
                    } else {
                        inputOutput.outPut("\n");
                    }
                } catch (ServerMainResponseException e) {
                    inputOutput.outPut("Script isn't valid: " + e.getMessage() + "\n");
                    withFile = false;
                    break;
                }
            }
            br.close();
        } catch (Exception e) {
            withFile = false;
            inputOutput.outPut("Script isn't valid: " + e.getMessage() + "\n");
        }finally {
            inputOutput.setReaderConsole(inputOutput.getSecondReaderConsole());
        }
    }



    private String invoke(String commandName) throws ServerMainResponseException {
        try {
            return commandInvoker.invoke(commandName);
        } catch (CommandValueException e) {
            throw new ServerMainResponseException("incorrect value of command: " + e.getMessage());
        } catch (NullPointerException ignored) {
            throw new ServerMainResponseException("incorrect command");
        } catch (CommandCollectionZeroException e) {
            throw new ServerMainResponseException("command is useless: " + e.getMessage());
        } catch (BadResponseException e) {
            throw new ServerMainResponseException("problem from server: " + e.getMessage());
        }
    }
}
