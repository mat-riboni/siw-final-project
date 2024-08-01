package it.uniroma3.vestiti.controller;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import it.uniroma3.vestiti.model.Negozio;
import it.uniroma3.vestiti.model.Prenotazione;
import it.uniroma3.vestiti.service.NegozioService;
import it.uniroma3.vestiti.service.PrenotazioneService;

@RestController
public class RESTfulController {

    @Autowired
    PrenotazioneService prenotazioneService;

    @Autowired
    NegozioService negozioService;

    @GetMapping("/negozio/{negozioId}/prenotazioni")
    public ResponseEntity<Map<String, Integer>> getMese2numeroPrenotazioni(@PathVariable Long negozioId) {
    	Map<String, Integer> mese2numeroPrenotazioni = new TreeMap<String, Integer>(new Comparator<>() {
    		
            @Override
            public int compare(String o1, String o2) {
                Month month1 = Month.valueOf(o1);
                Month month2 = Month.valueOf(o2);
                return month1.compareTo(month2);
            }
        });

        for (Month mese : Month.values()) {
            if (mese.getValue() <= LocalDate.now().getMonthValue()) {
                mese2numeroPrenotazioni.put(mese.toString(), 0);
            }
        }

        Optional<Negozio> optionalNegozio = this.negozioService.findById(negozioId);
        optionalNegozio.ifPresent(negozio -> {
            List<Prenotazione> prenotazioni = negozio.getPrenotazioni();
            for (Prenotazione p : prenotazioni) {
                if (p.getDataOra().getYear() == LocalDate.now().getYear()) {
                    String mese = p.getDataOra().getMonth().toString();
                    int numeroPrenotazioni = mese2numeroPrenotazioni.get(mese) + 1;
                    mese2numeroPrenotazioni.put(mese, numeroPrenotazioni);
                }
            }
        });

        if (optionalNegozio.isPresent()) {
            return ResponseEntity.ok(mese2numeroPrenotazioni);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
