package kr.hhplus.be.server.domain.point;

public class UserPointService {

    public void charge(UserPoint userPoint, Long amount){
        userPoint.charge(amount);
    }

    public  void validateCharfe(UserPoint userPoint, Long amount){

    }

}
