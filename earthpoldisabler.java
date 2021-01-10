public class EarthPolDisabler extends Module {
    public EarthPolDisabler() {
        super("EarthPolDisabler", "Allows you to do what ever you want!", Catagory.MISC);
    }
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        if(packet instanceof C03PacketPlayer) {
            if(mc.thePlayer.ticksExisted % 15 == 0) { 
              try {
                  ByteArrayOutputStream b = new ByteArrayOutputStream();
                  DataOutputStream out = new DataOutputStream(b);
                  out.writeUTF(mc.thePlayer.getGameProfile().getName());
                  PacketBuffer buf = new PacketBuffer(Unpooled.buffer());
                  buf.writeBytes(b.toByteArray());
                  Wrapper.mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C17PacketCustomPayload("matrix:geyser", buf)); // This is due to a config error by turning on Matrix geyser support
                } catch (IOException e) {

                }
            }
        }
    }
}
