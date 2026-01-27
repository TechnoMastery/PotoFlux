package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.actionRuns.regs.CloseRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartLogicRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartUiRunRegistry;

public class RegisterRunsEvent {
    public final CloseRunRegistry closeReg = new CloseRunRegistry();
    public final StartUiRunRegistry startUiReg = new StartUiRunRegistry();
    public final StartLogicRunRegistry startLogicReg = new StartLogicRunRegistry();
}
