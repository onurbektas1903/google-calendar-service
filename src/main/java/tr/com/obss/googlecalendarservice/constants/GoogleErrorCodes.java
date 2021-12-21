package tr.com.obss.googlecalendarservice.constants;

import com.google.api.client.http.HttpStatusCodes;
import org.springframework.http.HttpStatus;

import java.util.Set;

import static org.springframework.http.HttpStatus.OK;

public class GoogleErrorCodes {
    public static Set<Integer> BACK_OFF_ERROR_CODES = Set.of(403,404,429,500);
}
