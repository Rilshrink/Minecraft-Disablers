/*
 * This exploit is based off of the fact that you are offsetting your tp location the anticheat never recognizes you finished the teleport so it will forever exempt you!
*/
public class PineconeDisabler extends Module {
    public PineconeDisabler() {
        super("PineconeDisabler", "Disables the Pinecone Anticheat", Catagory.MISC);  
    }
    
    private boolean shouldTrigger = true;    
    
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        if(packet instanceof S08PacketPlayerPosLook) {
            ((S08PacketPlayerPosLook) packet).y += 0.001;
        }
    }
    
    public void onEnable() {
       shouldTrigger = true;
    }
    
    public void onWorldChange() {
        if(!this.getState()) return;
        shouldTrigger = true;
    }
    
    public void onUpdate() {
        if(!this.getState()) return;
        if(shouldTrigger && mc.thePlayer.onGround && mc.thePlayer.ticksExisted > 5) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 11, mc.thePlayer.posZ, true));
            shouldTrigger = false;
        }
    }
}
