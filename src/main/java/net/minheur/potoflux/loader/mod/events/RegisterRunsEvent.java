package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.actionRuns.regs.CloseRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartLogicRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartUiRunRegistry;

public class RegisterRunsEvent implements IEvent {
    public final CloseRunRegistry closeReg = new CloseRunRegistry();
    public final StartUiRunRegistry startUiReg = new StartUiRunRegistry();
    public final StartLogicRunRegistry startLogicReg = new StartLogicRunRegistry();

    @Override
    public void close() {
        closeReg.close();
        startUiReg.close();
        startLogicReg.close();
    }
}
