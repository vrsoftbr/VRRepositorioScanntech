package br.com.vrsoftware.vrreprocessarscanntech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("file:///vr/vr.properties")
public class DataBaseProperties {

    public static final String POSTGRESQL_DRIVER = "org.postgresql.Driver";

    @Value("${database.ip}")
    private String ip;

    @Value("${database.ipsec:}")
    private String ipSec;

    @Value("${database.usuario}")
    private String usuario;

    @Value("${database.senha}")
    private String senha;

    @Value("${database.porta}")
    private String porta;

    @Value("${database.nome}")
    private String nome;

    public String getUrl() {
        return String.format(
                "jdbc:postgresql://%s:%s/%s", getIp(), getPorta(), getNome()
        );
    }

    public String getIp() { return ip; }

    public String getUsuario() { return usuario; }

    public String getSenha() { return senha; }

    public String getPorta() { return porta; }

    public String getNome() { return nome; }
}
