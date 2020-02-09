package cn.edu.heuet.pagingdemofinal_java.db;

import cn.edu.heuet.pagingdemofinal_java.data.RepoBoundaryCallback;

public class CRUDApiImpl implements CRUDApi {
    @Override
    public void insertFinished(String errorMsg) {
        RepoBoundaryCallback.networkErrors.postValue(errorMsg);
        RepoBoundaryCallback.isRequestInProgress = false;
    }
}
