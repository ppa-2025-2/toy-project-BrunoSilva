package com.example.demo.Repository.Entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {

    public enum Status {
        PENDENTE,
        ANDAMENTO,
        CONCLUIDO,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private User responsavel;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private User destinatario;

    @ManyToOne
    @JoinColumn(name = "criador_id")
    private User criador;

    @Column(nullable = false, length = 255)
    private String objeto;

    @Column(nullable = false, length = 255)
    private String acao;

    @Column(nullable = false, length = 255)
    private String detalhes;

    @Column(nullable = false, length = 255)
    private String local;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ticket_observadores",
        joinColumns = @JoinColumn(name = "ticket_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> observadores = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE; // default

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public User getResponsavel() { return responsavel; }
    public void setResponsavel(User responsavel) { this.responsavel = responsavel; }

    public User getDestinatario() { return destinatario; }
    public void setDestinatario(User destinatario) { this.destinatario = destinatario; }

    public User getCriador() { return criador; }
    public void setCriador(User criador) { this.criador = criador; }

    public String getObjeto() { return objeto; }
    public void setObjeto(String objeto) { this.objeto = objeto; }

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public String getDetalhes() { return detalhes; }
    public void setDetalhes(String detalhes) { this.detalhes = detalhes; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public Set<User> getObservadores() { return observadores; }
    public void setObservadores(Set<User> observadores) { this.observadores = observadores; }
}
