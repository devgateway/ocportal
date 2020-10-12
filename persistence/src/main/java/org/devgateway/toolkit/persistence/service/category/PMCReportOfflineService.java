package org.devgateway.toolkit.persistence.service.category;

import org.devgateway.toolkit.persistence.dao.Person;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dto.PMCReportOffline;
import org.devgateway.toolkit.persistence.service.PersonService;

import javax.validation.Valid;
import java.util.List;

public interface PMCReportOfflineService {

    List<PMCReportOffline> getPMCReports(Long userId);
    
    Person loadPersonById(Long id, PersonService service);

    PMCReportOffline convertToOffline(PMCReport pmc);

    PMCReport convertToDaoDraft(@Valid PMCReportOffline pmco, Person person);

    PMCReport convertToDaoNonDraft(@Valid PMCReportOffline pmco, Person person);
}
