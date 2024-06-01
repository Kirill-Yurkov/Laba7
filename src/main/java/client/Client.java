package client;

import client.managers.CommandInvoker;
import client.managers.InputOutput;
import client.managers.TCPClient;
import client.utilities.TicketCreator;
import commons.exceptions.BadResponseException;
import commons.exceptions.CommandCollectionZeroException;
import commons.exceptions.CommandValueException;
import commons.exceptions.ServerMainResponseException;
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
        try {
            inputOutput.getReaderConsole().close();
            inputOutput.getWriter().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initialize(){
        clientOn = true;
        tcpClient.openConnection();
    }

    public void start() {
        boolean isCommandWas = true;
        initialize();
        while (clientOn) {
            try {
                if (isCommandWas){
                    inputOutput.outPut("Введите комманду (для справки используйте комманду help) \n~ ");
                } else {
                    inputOutput.outPut("~ ");
                }
                String commandFromConsole = inputOutput.inPutConsole();
                if (commandFromConsole == null){
                    inputOutput.outPut("\nПолучен сигнал завершения работы.");
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
