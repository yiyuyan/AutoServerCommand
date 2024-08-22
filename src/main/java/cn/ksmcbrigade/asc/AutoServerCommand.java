package cn.ksmcbrigade.asc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Mod(AutoServerCommand.MODID)
@Mod.EventBusSubscriber(modid = AutoServerCommand.MODID,value = Dist.DEDICATED_SERVER)
public class AutoServerCommand {

    public static final String MODID = "asc";
    public static final File config = new File("config/asc-config.json");
    public static Var var;

    public static long timerNow;

    public AutoServerCommand() throws IOException {
        MinecraftForge.EVENT_BUS.register(this);
        new File("config").mkdir();
        if(!config.exists()){
            save(false);
        }
        var = new Var(config);
        timerNow = var.getTimer();
    }

    @SubscribeEvent
    public static void ticks(TickEvent.ServerTickEvent event){
        if(timerNow==0){

            var.commands.forEach(c->{
                try {
                    event.getServer().getCommands().getDispatcher().execute(c,event.getServer().createCommandSourceStack());
                } catch (CommandSyntaxException e) {
                    System.out.println("Error in execute the command: "+e.getMessage()+" command: "+e.getInput());
                    e.printStackTrace();
                }
            });

            timerNow = var.getTimer();
        }
        else{
            timerNow--;
        }
    }

    public static void save(boolean ex) throws IOException {
        if(!config.exists() || ex){
            JsonObject obj = new JsonObject();
            obj.addProperty("timer",var==null?Var.DEF.getTimer():var.getTimer());
            JsonArray array = new JsonArray();
            if(var!=null){
                var.commands.forEach(array::add);
            }
            obj.add("commands",array);
            FileUtils.writeStringToFile(config, obj.toString());
        }
    }
}
