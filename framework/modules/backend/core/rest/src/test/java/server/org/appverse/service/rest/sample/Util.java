package server.org.appverse.service.rest.sample;

import java.util.HashSet;
import java.util.Set;

public class Util {

	public static Set<Integer> parseNumIds(final String ids) {
		if (ids == null)
			return null;

		Set<Integer> valid = new HashSet<Integer>();
		final String regex = ids.contains(",") ? "," : "\\|";
		for (String s : ids.split(regex)) {
			if (!s.isEmpty())
				valid.add(Integer.valueOf(s));
		}
		return valid;
	}

	public static Integer[] parseAsLongArray(final String ids) {
		if (ids == null)
			return null;

		return parseNumIds(ids).toArray(new Integer[0]);
	}
}
