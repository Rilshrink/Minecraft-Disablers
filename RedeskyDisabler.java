public class RedeskyDisabler extends Module {
    public RedeskyDisabler() {
        super("RedeskyDisabler", "'Disables' Redesky's Anticheat", Catagory.MISC); // Recommended to set your fly speed extremely high, its really slow
    }
    public float rotYaw = 0;
    public float rotPitch = 0;
    public void onEnable() {
        rotYaw = mc.thePlayer.rotationYaw;
        rotPitch = mc.thePlayer.rotationPitch;
    }
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        
        Packet packet = event.getPacket();
        
        if(packet instanceof C03PacketPlayer) {
            C03PacketPlayer c03 = (C03PacketPlayer)packet;
            float x = RandomUtils.randFloat(10.1f, 50F);
            float y = RandomUtils.randFloat(10.1f, 50F);
            float z = RandomUtils.randFloat(10.1f, 50F);
            
            if (mc.thePlayer.ticksExisted % 4 == 0) {
		c03.x += (RandomUtils.random.nextBoolean() ? x : -x);
		c03.y += (RandomUtils.random.nextBoolean() ? y : -y);
		c03.z += (RandomUtils.random.nextBoolean() ? z : -z);
	    }
	    if(c03.getRotating()) {
		c03.yaw = mc.thePlayer.ticksExisted % 2 == 0 ? startYaw : mc.thePlayer.lastReportedYaw;
		c03.pitch = mc.thePlayer.ticksExisted % 2 == 0 ? startPitch : mc.thePlayer.lastReportedPitch;
	    }
        }
    }
    public void onUpdate() {
        mc.timer.timerSpeed = 0.12F;
    }
}
