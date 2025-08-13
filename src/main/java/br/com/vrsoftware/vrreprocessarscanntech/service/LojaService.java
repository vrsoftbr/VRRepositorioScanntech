package br.com.vrsoftware.vrreprocessarscanntech.service;

import br.com.vrsoftware.vrreprocessarscanntech.repository.LojaDAO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LojaService {

    private final LojaDAO lojaDao;

    public LojaService(LojaDAO lojaDao) {
        this.lojaDao = lojaDao;
    }

    /**
     * Método responsável por listar todas as lojas disponíveis no sistema.
     *
     * @return lista de lojas no formato {@link List} de {@link Map}, onde cada mapa
     *         contém os atributos da loja (ex.: "id" e "descricao")
     */
    public List<Map<String, Object>> listar() {
        return lojaDao.listarLojas();
    }

    /**
     * Método responsável por criar um mapeamento entre o ID da loja e seu nome.
     *
     * @return mapa contendo o ID da loja como chave e o nome (descrição) como valor
     *
     * @see LojaDAO#listarLojas()
     */
    public Map<Integer, String> mapearIdParaNome() {
        return lojaDao.listarLojas().stream()
                .collect(Collectors.toMap(
                        l -> (Integer) l.get("id"),
                        l -> (String) l.get("descricao")
                ));
    }
}
