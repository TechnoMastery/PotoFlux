package net.minheur.potoflux.loader.mod.events;

import net.minheur.potoflux.actionRuns.regs.CloseRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartLogicRunRegistry;
import net.minheur.potoflux.actionRuns.regs.StartUiRunRegistry;

/**
 * Register runs event class.
 */
public class RegisterRunsEvent implements IEvent {
    /**
     * The close reg field.
     */
    public final CloseRunRegistry closeReg = new CloseRunRegistry();
    /**
     * The start ui reg field.
     */
    public final StartUiRunRegistry startUiReg = new StartUiRunRegistry();
    /**
     * The start logic reg field.
     */
    public final StartLogicRunRegistry startLogicReg = new StartLogicRunRegistry();

    @Override
    public void close() {
        closeReg.close();
        startUiReg.close();
        startLogicReg.close();
    }
}
