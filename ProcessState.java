/**
 * Enumeração que define os possíveis estados de um processo no sistema.
 * Implementa o modelo de estados clássico de sistemas operacionais.
 */
public enum ProcessState {
    NEW, // Processo criado mas ainda não carregado na memória
    READY, // Processo carregado e pronto para execução (na fila de prontos)
    RUNNING, // Processo atualmente em execução na CPU
    BLOCKED, // Processo bloqueado aguardando algum evento (I/O, etc.)
    TERMINATED // Processo finalizado e aguardando limpeza de recursos
}