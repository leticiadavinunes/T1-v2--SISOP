public class SO {
    public InterruptHandling ih;
    public SysCallHandling sc;
    public Utilities utils;

    public SO(HW hw) {
        ih = new InterruptHandling(hw);
        sc = new SysCallHandling(hw);
        hw.cpu.setAddressOfHandlers(ih, sc);
        utils = new Utilities(hw);
    }
}
