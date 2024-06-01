package client.managers;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;

public class InputOutput {
    @Getter
    private BufferedReader readerConsole;
    @Getter
    private BufferedOutputStream writer;
    @Getter
    private BufferedReader secondReaderConsole;
    private String lastOut = "";

    public void setReaderConsole(BufferedReader readerConsole) {
        this.readerConsole = readerConsole;
    }

    public void setSecondReaderConsole(BufferedReader secondReaderConsole) {
        this.secondReaderConsole = secondReaderConsole;
    }

    public void setWriter(BufferedOutputStream writer) {
        if(writer!=null){
            try{
                this.writer.close();
            } catch (IOException | NullPointerException ignored){}
            this.writer = writer;
        }
    }

    public String inPutConsole() {
        if(readerConsole!=null){
            try {
                return readerConsole.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void outPut(String text) {
        try {
            if(!lastOut.equals("\n") || !text.equals("\n")){
                writer.write(text.getBytes());
                writer.flush();
                lastOut = text;
            }
        } catch (IOException ignored) {
        }
    }
}