package Out_Box;
import com.sun.speech.freetts.Voice;  
import com.sun.speech.freetts.VoiceManager;  
public class TextToSpeech
{  
public static void main(String args[])   
{  
Voice voice;  
voice = VoiceManager.getInstance().getVoice("kevin16");  
System.out.println( VoiceManager.getInstance());
if (voice != null)   
{  
voice.allocate();  
}  
try   
{  
voice.setRate(100);  
voice.setPitch(200);  
voice.setVolume(10);   
voice.speak("thanda lag rha he");  
}  
catch(Exception e)  
{  
e.printStackTrace();  
}  
}  
}  
