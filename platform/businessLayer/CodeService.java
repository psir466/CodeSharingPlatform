package platform.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import platform.logic.MCode;
import platform.repository.MCodeRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CodeService {

    private final MCodeRepository mCodeRepository;

    @Autowired
    public CodeService(MCodeRepository mCodeRepository) {
        this.mCodeRepository = mCodeRepository;
    }

    public Optional<MCode> findCodeById(long id) {

        if (this.mCodeRepository.findById(id).isPresent()) {

            return this.mCodeRepository.findById(id);

        } else {

            return null;

        }
    }

    public List<MCode> findAllCode() {

        List<MCode> mCodeList = new ArrayList<MCode>();

        // ce qui ressort de findAll est un Iterable pas une liste il faut donc tout mettre dans une liste
        this.mCodeRepository.findAll().forEach(mCodeList::add);

        return mCodeList;
    }

    public MCode saveCode(MCode mCode) {

        return this.mCodeRepository.save(mCode);
    }


    public Optional<MCode> findCodeByUuid(String uuidCode) {

        if (this.mCodeRepository.findByUuidCode(uuidCode).isPresent()) {

            return this.mCodeRepository.findByUuidCode(uuidCode);


        } else {

            return null;

        }
    }

    public boolean possibleToViewCode(String uuidCode) {

        boolean okToviewCode = false;

        if (this.mCodeRepository.findByUuidCode(uuidCode).isPresent()) {

            if (this.mCodeRepository.findByUuidCode(uuidCode).get().isSecretCodeTime() ||
            this.findCodeByUuid(uuidCode).get().isSecretCodeViews()) {
                if (manageView(uuidCode)) {

                    okToviewCode = true;
                }
            }else{

                okToviewCode = true;
            }
        }

        return okToviewCode;
    }

    private Boolean manageView(String uuidCode) {

        Boolean okToSeeTheCode = false;

        System.out.println("Uuid " + uuidCode );

        System.out.println(" présent : " + this.mCodeRepository.findByUuidCode(uuidCode).isPresent());

        if (this.mCodeRepository.findByUuidCode(uuidCode).isPresent()) {

            MCode mCode = this.mCodeRepository.findByUuidCode(uuidCode).get();


            System.out.println("Time : " + mCode.isSecretCodeTime());

            System.out.println("Views : " + mCode.isSecretCodeViews());

            System.out.println("Views nb : " + mCode.getViews());

            if(mCode.isSecretCodeViews()) {
                int views = mCode.getViews();

                if (views - 1 >= 0) {

                    mCode.setViews(views - 1);

                    this.mCodeRepository.save(mCode);

                    okToSeeTheCode = true;

                } else {

                    // des fois ne marche pas en faisant la vérif auto ???
                  //  this.mCodeRepository.deleteByUuidCode(uuidCode);

                    this.mCodeRepository.delete(mCode);
                }
            }

            if(mCode.isSecretCodeTime()) {

                LocalDateTime ldt = LocalDateTime.of(mCode.getCodeDate(), mCode.getCodeTime());

                LocalDateTime ldtnow = LocalDateTime.now();

                long seconds = ldt.until(ldtnow, ChronoUnit.SECONDS);

                int secondsCode = mCode.getTime();

                if (secondsCode >= seconds) {

                    mCode.setTime(secondsCode - (int) seconds);

                    this.mCodeRepository.save(mCode);

                    okToSeeTheCode = true;

                } else {

                    this.mCodeRepository.delete(mCode);

                }
            }

        }

        return okToSeeTheCode;
    }


}
