package com.ecust.service;

import com.ecust.domain.Medicines;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ecust.dto.MedicinesDto;
import com.ecust.vo.DataGridView;

import java.util.List;

public interface MedicinesService{
    DataGridView listForPage(MedicinesDto medicinesDto);

    int addMedicines(MedicinesDto medicinesDto);

    Medicines queryMedicinesById(Long medicinesId);

    int updateMedicines(MedicinesDto medicinesDto);

    int deleteMedicinesByIds(Long[] medicinesIds);

    List<Medicines> queryAllMedicines();

    int updateMedicinesStorage(Long medicinesId, Long medicinesStockNum);

    int deductionMedicinesStorage(Long medicinesId, Long num);
}
