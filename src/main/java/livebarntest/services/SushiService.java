package livebarntest.services;

import livebarntest.entity.SushiType;

public interface SushiService {
    SushiType findByName(String name);
}
