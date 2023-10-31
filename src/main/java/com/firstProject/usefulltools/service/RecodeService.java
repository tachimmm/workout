package com.firstProject.usefulltools.service;

import com.firstProject.usefulltools.entity.RecodeInfo;
import com.firstProject.usefulltools.form.RecodeForm;
import com.firstProject.usefulltools.form.RecodeSearchForm;
import com.firstProject.usefulltools.repository.RecodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecodeService {

    @Autowired
    RecodeRepository recodeRepository;

    public List<RecodeInfo> searchAll() {

        return recodeRepository.findAll();

    }


    public void create(RecodeForm recodeForm) {

        RecodeInfo recodeInfo = createUser(recodeForm);
        recodeRepository.save(recodeInfo);

    }


    public List<RecodeInfo> findByUsername(String username) {

        return recodeRepository.findByUsername(username);

    }


    public void deleteDataById(Long id) {
        
        Long save_id = id;

        recodeRepository.deleteById(save_id);

    }


    public List<RecodeInfo> findLatesRecodeInfo(String username){

        return recodeRepository.findByUsernameAndLastDate(username);

    }

    
    public List<RecodeInfo> findByUsernameSearch(RecodeSearchForm recodeSearchForm) {

        String username = recodeSearchForm.getUsername();
        String item = recodeSearchForm.getItem();
        String date_column = recodeSearchForm.getDate_column();

        if ((item == null || item.isEmpty()) && date_column != null) {

            return recodeRepository.findByUsernameAndDateColumn(username, date_column);

        } else if (item != null && (date_column == null || date_column.isEmpty())) {

            return recodeRepository.findByUsernameAndItem(username, item);

        } else {

            return recodeRepository.findByUsernameAndItemAndDateColumn(username, item, date_column);

        }

    }

    private RecodeInfo createUser(RecodeForm recodeForm) {

        RecodeInfo recodeInfo = new RecodeInfo();

        recodeInfo.setUsername(recodeForm.getUsername());
        recodeInfo.setEvent(recodeForm.getEvent());
        recodeInfo.setItem(recodeForm.getItem());
        recodeInfo.setDate_column(recodeForm.getDate());
        recodeInfo.setSet_column(recodeForm.getSet());
        recodeInfo.setRep(recodeForm.getRep());
        recodeInfo.setWeight(recodeForm.getWeight());
        
        return recodeInfo;
    }
}
