//F
(script = registerScript({
    name: "Disabler",
    authors: ["Rilshrink"],
    version: "1.0"
})).import("Core.lib");
module = {
    category: "Exploit",
    description: "Bye Bye!",
    values: mode = new (Java.extend(ListValue)) ("Mode", ["Lunar", "Kauri", "OnlyMC", "HazelMC"], "Lunar"),
    onPacket: function (e) {
        switch(mode.get()) {
            case "Kauri":
                if(e.getPacket() instanceof C0FPacketConfirmTransaction) {
                    e.cancelEvent();
                }
                break;
            case "Lunar":
                if(e.getPacket() instanceof C17PacketCustomPayload) {
                    if(e.getPacket().getChannelName() == "MC|Brand") {
                        var b = new (Java.type("java.io.ByteArrayOutputStream"))();
                        var message = (Java.type("io.netty.buffer.Unpooled")).buffer();
                        message.writeBytes("Lunar-Client".getBytes());
                        sendPacket(new C17PacketCustomPayload("REGISTER", new (Java.type("net.minecraft.network.PacketBuffer")(message))));
                    }
                }
            case "OnlyMC":
                if(e.getPacket() instanceof C0FPacketConfirmTransaction) {
                    Transactions.add(e.getPacket());
                    e.cancelEvent();
                }
                if(e.getPacket() instanceof C00PacketKeepAlive) {
                    e.getPacket().key-=1337;
                }
                if(e.getPacket() instanceof C03PacketPlayer) {
                    sendPacket(new C0CPacketInput());
                }
                break;
            case "HazelMC":
                if(e.getPacket() instanceof C0FPacketConfirmTransaction) {
                    Transactions.add(e.getPacket());
                    e.cancelEvent();
                }
                if(e.getPacket() instanceof C00PacketKeepAlive) {
                    KeepAlives.add(e.getPacket());
                    e.cancelEvent();
                }
                if(e.getPacket() instanceof C03PacketPlayer) {
                    sendPacket(new C0CPacketInput());
                }
                break;
        }
    },
    onWorld: function() {
        reset();
    },
    onUpdate: function () {
        switch(mode.get()) {
            case "OnlyMC":
            case "Lunar":
                if(mc.thePlayer.ticksExisted % 120 == 0 && Transactions.size() > currentTrans) {
                    sendPacket(Transactions.toArray()[currentTrans++]);
                }
                if(mc.thePlayer.ticksExisted % 25 == 0) {
                    sendPacket(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 11, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                }
                if(mc.thePlayer.ticksExisted % 300 == 0) {
                    reset();
                }
                break;
            case "HazelMC":
                sendPacket(new C00PacketKeepAlive(0));
                if(Transactions.size() > currentTrans) {
                    sendPacket(Transactions.toArray()[currentTrans++]);
                }
                if(mc.thePlayer.ticksExisted % 100 == 0) {
                    for(var i = 0; i < KeepAlives.size(); i++) {
                        var packet = KeepAlives.toArray()[i];
                        if(packet != null) {
                            sendPacket(packet);
                        }
                    }
                }
                break;
        }
    },
    onEnable: function () {

    },
    onDisable: function () {
        reset();
    }
};

var KeepAlives = new (Java.type("java.util.concurrent.LinkedBlockingQueue"))();
var Transactions = new (Java.type("java.util.concurrent.LinkedBlockingQueue"))();
var currentTrans = 0;
var reset = function() {
    currentTrans = 0;
    KeepAlives.reset();
    Transactions.reset();
}