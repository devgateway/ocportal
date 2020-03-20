package org.devgateway.toolkit.persistence.service.sms;

import org.devgateway.toolkit.persistence.dao.DBConstants;
import org.devgateway.toolkit.persistence.dao.form.MEReport;
import org.devgateway.toolkit.persistence.dao.form.PMCReport;
import org.devgateway.toolkit.persistence.dao.form.TenderProcess;
import org.devgateway.toolkit.persistence.dao.sms.SMSMessage;
import org.devgateway.toolkit.persistence.repository.norepository.BaseJpaRepository;
import org.devgateway.toolkit.persistence.repository.sms.SMSMessageRepository;
import org.devgateway.toolkit.persistence.service.BaseJpaServiceImpl;
import org.devgateway.toolkit.persistence.service.form.TenderProcessService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.devgateway.toolkit.persistence.dao.DBConstants.SMSCommands.INFO;
import static org.devgateway.toolkit.persistence.dao.DBConstants.SMSCommands.REPORT;

/**
 * @author mpostelnicu
 */
@Service
@Transactional
public class SMSMessageServiceImpl extends BaseJpaServiceImpl<SMSMessage> implements SMSMessageService {

    @Value("${infobip.key}")
    private String infobipKey;

    private static final Logger logger = LoggerFactory.getLogger(SMSMessageServiceImpl.class);

    @Autowired
    private SMSMessageRepository repository;

    @Autowired
    private TenderProcessService tenderProcessService;

    @Override
    protected BaseJpaRepository<SMSMessage, Long> repository() {
        return repository;
    }

    @Override
    public SMSMessage newInstance() {
        return new SMSMessage();
    }

    @Override
    @Async
    @Scheduled(cron = "0 0/10 * * * ?")
    public void processSMSQueue() {
        repository.findByProcessedIsFalse().forEach(this::processAndPersistSMSResult);
    }

    @Override
    public synchronized void processAndPersistSMSResult(SMSMessage m) {
        boolean ret = processSMS(m);
        if (ret) {
            m.setProcessed(true);
            repository.saveAndFlush(m);
        }
    }

    @Override
    public boolean sendSMS(String destination, String text) {
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        msg.put("from", "40014");
        Map<String, Object> dest = new HashMap<>();
        dest.put("to", destination);
        msg.put("destinations", Arrays.asList(dest));
        msg.put("text", text);
        root.put("messages", msg);
        JSONObject jo = new JSONObject(root);

        final String baseUrl = "https://ekllr.api.infobip.com/sms/2/text/advanced";
        URI uri = null;
        try {
            uri = new URI(baseUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "App " + infobipKey);
        HttpEntity<String> request = new HttpEntity<>(jo.toString(), headers);

        logger.info("Sending SMS " + jo.toString());
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            logger.error("Error " + response.getStatusCode().value() + "sending SMS " + jo.toString());
            return false;
        }

        return true;
    }


    @Override
    public boolean processSMS(SMSMessage message) {
        if (message.getTextNoKeyword().toUpperCase().startsWith(INFO)) {
            return executeINFOCommand(message);
        }
        if (message.getTextNoKeyword().toUpperCase().startsWith(REPORT)) {
            return executeREPORTCommand(message);
        }
        return true;
    }

    @Override
    public boolean executeINFOCommand(SMSMessage message) {
        String tenderProcessStr = message.getTextNoKeyword().substring(INFO.length()).trim();
        Long tenderProcessId = null;

        try {
            tenderProcessId = Long.valueOf(tenderProcessStr);
        } catch (NumberFormatException e) {
            logger.error("Error executing INFO command: number format exception for process id:" + tenderProcessStr);
            return true;
        }

        Optional<TenderProcess> byId = tenderProcessService.findById(tenderProcessId);
        if (byId.isPresent() && byId.get().getStatus().equals(DBConstants.Status.APPROVED)
                && byId.get().getSingleTender() != null && byId.get().getSingleTender().getStatus()
                .equals(DBConstants.Status.APPROVED)) {
            // send sms back to host
            StringBuffer sb = new StringBuffer("Tender Code: ");
            sb.append(byId.get().getId()).append("\\n");
            sb.append("Tender Name: ").append(byId.get().getSingleTender().getTenderTitle()).append("\\n");
            Optional<MEReport> lastMEReport = byId.get().getMeReports().stream()
                    .filter(r -> r.getStatus().equals(DBConstants.Status.APPROVED))
                    .sorted(Comparator.comparing(MEReport::getApprovedDate).reversed()).findFirst();
            sb.append("M&E Status: ");
            if (lastMEReport.isPresent()) {
                sb.append(lastMEReport.get().getMeStatus().getLabel());
            } else {
                sb.append("Not available");
            }
            sb.append("\\n");

            Optional<PMCReport> lastPMCReport = byId.get().getPmcReports().stream()
                    .filter(r -> r.getStatus().equals(DBConstants.Status.APPROVED))
                    .sorted(Comparator.comparing(PMCReport::getApprovedDate).reversed()).findFirst();
            sb.append("PMC Status: ");
            if (lastPMCReport.isPresent()) {
                sb.append(lastPMCReport.get().getPmcStatus().getLabel());
            } else {
                sb.append("Not available");
            }
            sb.append("\\n");
            logger.info(sb.toString());
            return sendSMS(message.getFrom(), sb.toString());
        } else {
            return true;
        }

    }

    @Override
    public boolean executeREPORTCommand(SMSMessage message) {
        return false;
    }
}

