package cn.ksmcbrigade.asc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Var {

    public static Var DEF = new Var(12000L,new ArrayList<>());

    public ArrayList<String> commands = new ArrayList<>();

    private long timer = 12000L;  //ten minutes

    public Var(File file) throws IOException {
        JsonObject object = JsonParser.parseString(FileUtils.readFileToString(file)).getAsJsonObject();
        ArrayList<String> list = new ArrayList<>();
        object.getAsJsonArray("commands").forEach(c->list.add(c.getAsString()));
        this.timer = object.get("timer").getAsLong();
        this.commands = list;
    }

    public Var(long timer, ArrayList<String> commands){
        this.commands = commands;
        this.timer = timer;
    }

    public void setTimer(long timer) {
        this.timer = timer;
    }

    public long getTimer() {
        return timer;
    }
}
