package net.minheur.potoflux.utils.ressourcelocation;

/**
 * This is thrown when there's an error in a {@link ResourceLocation}
 */
public class ResourceLocationException extends RuntimeException {
  /**
   * Throws the exception
   * @param pMessage message to send on throwing
   */
  public ResourceLocationException(String pMessage) {
    super(pMessage);
  }

  /**
   * Throws the exception with a cause
   * @param pMessage message to send on throwing
   * @param pCause cause of throwing
   */
  public ResourceLocationException(String pMessage, Throwable pCause) {
    super(pMessage, pCause);
  }
}
