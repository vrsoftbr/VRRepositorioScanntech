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

    public List<Map<String, Object>> listar() {
        return lojaDao.listarLojas();
    }

    public Map<Integer, String> mapearIdParaNome() {
        return lojaDao.listarLojas().stream()
                .collect(Collectors.toMap(
                        l -> (Integer) l.get("id"),
                        l -> (String) l.get("descricao")
                ));
    }
}
