package com.movie.trailer.movieservice.controller;

import com.movie.trailer.movieservice.beans.RequestBean;
import com.movie.trailer.movieservice.exception.beans.TrailersNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Slf4j
@ControllerAdvice(assignableTypes = {MovieServiceController.class})
public class MovieServiceControllerAdvice {

    private static final Set<String> ISO_COUNTRIES = new HashSet<>
            (Arrays.asList(Locale.getISOCountries()));
    private static final Set<String> ISO_LANGUAGES = new HashSet<>
            (Arrays.asList(Locale.getISOLanguages()));
    @ModelAttribute
    public RequestBean processRequestParameters(
            @RequestParam String query,
            @RequestParam(required= false) String year,
            @RequestParam String country,
            @RequestParam(defaultValue = "en", required = false) String language,
            @RequestParam(required = false) String page
    ) throws TrailersNotFoundException {
        validateInput(query, year, country, language, page);
        var rb = new RequestBean();
        log.info("query ={}",query);
        rb.setQuery(query.trim().replaceAll("\\+"," "));
        rb.setYear(year!=null && !year.isBlank()? year.trim(): year);
        rb.setCountry(country);
        rb.setLanguage(language);
        rb.setPage(page == null ? 0: Integer.parseInt(page));
        return rb;

    }

    private void validateInput(String query, String year, String country, String language, String page) throws TrailersNotFoundException {
        if(query.trim().length() <3 || query.trim().length()>50)
            throw new TrailersNotFoundException("3-50 characters are allowed for query.", "query");
        try{
            if(year!=null && !year.isBlank()){
                Integer intYear = Integer.parseInt(year.trim());
                if(intYear<1870 || intYear > Calendar.getInstance().get(Calendar.YEAR)+5)
                    throw new TrailersNotFoundException("Date is not in correct range.", "year");
            }
        }catch(NumberFormatException ex){
            throw new TrailersNotFoundException("Date is not in correct format.", "year");
        }
        if(!ISO_COUNTRIES.contains(country))
            throw new TrailersNotFoundException("Country is not valid", "country");

        if(!ISO_LANGUAGES.contains(language))
            throw new TrailersNotFoundException("Language is not valid", "language");

        try{
            if(page!=null && !page.isBlank()){
                Integer intPage = Integer.parseInt(page.trim());
                if(0> intPage)
                    throw new TrailersNotFoundException("Page is not a valid number", "page");
            }
        }catch(NumberFormatException ex){
            throw new TrailersNotFoundException("Date is not in correct format.", "year");
        }

    }
}
