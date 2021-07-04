///api_version=2

/*
    Requires core.lib to use!
*/

(script = registerScript({
    name: "Disabler",
    authors: ["Rilshrink"],
    version: "1.1" // Actually remembered to change it!
})).import("Core.lib");

module = {
    category: "Exploit",
    description: "Anticheat no work",
    values: [mode = value.createList("Mode", ["Kauri", "Old Verus", "Old Ghostly", "Verus Combat", "Old Matrix", "Riding", "Spectate", "Basic", "NoPayload", "Offset", "C03 Cancel", "C06 Only", "NullPlace"], "Kauri")],
    onPacket: function (e) {
        switch(mode.get()) {
			case "Basic":
				if(e.getPacket() instanceof C0FPacketConfirmTransaction || e.getPacket() instanceof C00PacketKeepAlive) {
					e.cancelEvent();
				}
				break;
			case "NoPayload":
				if(e.getPacket() instanceof C17PacketCustomPayload) {
					e.cancelEvent();
				}
				break;
			case "C06 Only":
				if(e.getPacket() instanceof C03PacketPlayer) {
					sendPacket(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
					e.cancelEvent();
				}
				break;
			case "C03 Cancel":
				if(e.getPacket() instanceof C03PacketPlayer) {
					if(mc.thePlayer.ticksExisted % 3 != 0) {
						e.cancelEvent();
					}
				}
				break;
			case "Offset":
				if(e.getPacket() instanceof C03PacketPlayer) {
					if(mc.thePlayer.ticksExisted < 20 && mc.thePlayer.ticksExisted % 2 == 0) {
						sendPacket(new C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 21, mc.thePlayer.posZ, true));
					}
				} else if(e.getPacket() instanceof S08PacketPlayerPosLook) {
					e.getPacket().y += 0.001;
				}
				break;
            case "Kauri":
                if(e.getPacket() instanceof C0FPacketConfirmTransaction) {
                    e.cancelEvent();
                }
                break;
            case "Verus Combat":
                if (e.getPacket() instanceof C0FPacketConfirmTransaction) {
                    if(currentTrans++>0) e.cancelEvent();
                } else if(e.getPacket() instanceof C0BPacketEntityAction) {
                    e.cancelEvent();
                } 
                break;
			case "Spectate":
				if(e.getPacket() instanceof C03PacketPlayer) {
					sendPacket(new C18PacketSpectate(UUID.randomUUID()));
				}
				break;
			case "Riding":
				if(e.getPacket() instanceof C03PacketPlayer) {
					sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, mc.thePlayer.movementInput.jump, mc.thePlayer.movementInput.sneak));
				}
				break;
			case "Old Ghostly":
				if(e.getPacket() instanceof C03PacketPlayer) {
					sendPacket(new C0CPacketInput());
				} else if(e.getPacket() instanceof C0FPacketConfirmTransaction || 
				          e.getPacket() instanceof C00PacketKeepAlive) {
					e.cancelEvent();
				}
				break;
            case "Old Verus":
                if(e.getPacket() instanceof C0FPacketConfirmTransaction) {
                    Transactions.add(e.getPacket());
                    e.cancelEvent();
                }
                if(e.getPacket() instanceof C00PacketKeepAlive) {
                    sendPacket(new C00PacketKeepAlive(e.getPacket().getKey() - 1));
                    e.cancelEvent();
                }
                if(e.getPacket() instanceof C03PacketPlayer) {
                    sendPacket(new C0CPacketInput()); // Disables old verus speed checks.
                }
                break;
			case "Old Matrix":
				if(e.getPacket() instanceof C03PacketPlayer) {
					if(mc.thePlayer.ticksExisted % 15 == 0) { // Technically only have to send once, but to be sure it exempts you just send once every 15 ticks.
						var b = new (Java.type("java.io.ByteArrayOutputStream"))();
						var out = new (Java.type("java.io.DataOutputStream"))(b);
						out.writeUTF(mc.thePlayer.getGameProfile().getName()); // Username of player to exempt
						var buf = new PacketBuffer(((Java.type("io.netty.buffer.Unpooled")).buffer()));
						buf.writeBytes(b.toByteArray());
						sendPacket(new C17PacketCustomPayload("matrix:geyser", buf));
					}
				}
				break;
        }
    },
    onWorld: function(ev) {
        reset();
    },
    onUpdate: function () {
        DisablerModule.tag = mode.get();
        switch(mode.get()) {
            case "Old Verus":
                if(mc.thePlayer.ticksExisted % 120 == 0 && Transactions.size() > currentTrans) {
                    sendPacket(Transactions.get[currentTrans++]);
                }
                if(mc.thePlayer.ticksExisted % 25 == 0) { // This is used to disable old verus flight checks
                    sendPacket(new C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - 11, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
                }
                if(mc.thePlayer.ticksExisted % 300 == 0) {
                    reset();
                }
                break;
			case "NullPlace":
				sendPacket(new C08PacketPlayerBlockPlacement(new BlockPos(Double.NaN, Double.NaN, Double.NaN), 1, null, 0, 0, 0));
				break;
        }
    },
    onEnable: function () {

    },
    onDisable: function () {
        reset();
    }
};

var KeepAlives = new (Java.type("java.util.ArrayList"))();
var Transactions = new (Java.type("java.util.ArrayList"))();
var currentTrans = 0;
var reset = function() {
    currentTrans = 0;
    KeepAlives.clear();
    Transactions.clear();
}
