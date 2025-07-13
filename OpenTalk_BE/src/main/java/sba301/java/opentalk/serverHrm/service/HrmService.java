package sba301.java.opentalk.serverHrm.service;

import sba301.java.opentalk.exception.AppException;
import sba301.java.opentalk.serverHrm.model.UserFromHRM;
import sba301.java.opentalk.serverHrm.model.UserHRM;

import java.util.List;

public interface HrmService {
    List<UserFromHRM> findAllUsersFormHRM();

    public List<UserHRM> fetchUsersFromHrm();

    public void syncUsersFromHrm() throws AppException;
}
