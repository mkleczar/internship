package pl.alior;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.function.Function;

@Log4j2
public class Main {

    /*
    DISCLAIMER:
    To complete business and HR requirements we use local language words with non ASCII characters for
    classes and variables names.
    In real code we would never do that.
    No developers were harmed in the making of this code.
     */

    private static final String DEFAULT_NAME = "Programista";

    public static void main(String[] args) {
        String ty = ArrayUtils.isEmpty(args) || StringUtils.isEmpty(args[0]) ?
                DEFAULT_NAME : args[0];

        Optional.ofNullable(ty)
                .map(Kandydat::new)
                .flatMap(kandydat -> new Main().rekrutacja(kandydat))
                .ifPresentOrElse(
                        stazysta -> log.info("Wygląda na to, że wszystko poszło świetnie, witamy na pokładzie: {}!", stazysta),
                        () -> log.info("Może spróbujesz jeszcze raz?"));
    }

    private Optional<Stażysta> rekrutacja(Kandydat kandydat) {
        log.info("Instrukcja dla: {} \"Jak do nas dołączyć?\"", kandydat.getName());
        return Optional.of(kandydat)
                .map(wypełnijAnkietę)
                .map(rozwiążZadanieRekrutacyjne)
                .flatMap(spotkajmySięIPorozmawiajmy)
                ;
    }


    @AllArgsConstructor
    @Getter
    private static class Kandydat {
        private String name;
    }

    @AllArgsConstructor
    @Getter
    private static class Ankieta {
        private Kandydat kandydat;
    }

    @Getter
    private static class Zadanie {
        private final String subject = "Zadanie rekrutacyjne";
    }

    @AllArgsConstructor
    @ToString
    private static class Stażysta {
        private String name;
    }



    private final Function<Kandydat, Ankieta> wypełnijAnkietę = kandydat -> {
        log.info("Kliknij w link i wypełnij ankietę");
        return new Ankieta(kandydat);
    };

    private final Function<Ankieta, Pair<Ankieta, Zadanie>> rozwiążZadanieRekrutacyjne = ankieta -> {
        log.info("Wyślemy Ci krótkie zadanie rekrutacyjne");
        return Pair.of(ankieta, new Zadanie());
    };

    private final Function<Pair<Ankieta, Zadanie>, Optional<Stażysta>> spotkajmySięIPorozmawiajmy = pair -> {
        log.info("Spotkamy się i porozmawiamy: opowiemy o pracy w Aliorze, Ty opowiesz o sobie i pokażesz jak rozwiązałeś: {}",
                pair.getRight().getSubject());
        return Optional.of(new Stażysta(pair.getLeft().getKandydat().getName()));
    };
}
