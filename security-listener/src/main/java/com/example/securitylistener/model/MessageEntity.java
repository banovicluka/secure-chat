package com.example.securitylistener.model;

import jakarta.persistence.*;

@Entity
@Table(name = "message", schema = "security", catalog = "")
public class MessageEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Integer id;
    @Basic
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "id_sender")
    private Integer idSender;
    @Basic
    @Column(name = "id_reciever")
    private Integer idReciever;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIdSender() {
        return idSender;
    }

    public void setIdSender(Integer idSender) {
        this.idSender = idSender;
    }

    public Integer getIdReciever() {
        return idReciever;
    }

    public void setIdReciever(Integer idReciever) {
        this.idReciever = idReciever;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageEntity that = (MessageEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (idSender != null ? !idSender.equals(that.idSender) : that.idSender != null) return false;
        if (idReciever != null ? !idReciever.equals(that.idReciever) : that.idReciever != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (idSender != null ? idSender.hashCode() : 0);
        result = 31 * result + (idReciever != null ? idReciever.hashCode() : 0);
        return result;
    }
}
