package com.github.decyg.CrAgg.database.indexer

import com.github.decyg.CrAgg.cif.results.CIFDetailedResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by declan on 17/03/2017.
 */
@Service
class CIFServiceImpl : CIFService {

    lateinit var cifRepo : CIFRepository

    @Autowired
    fun setCIFRepository(cifRepo : CIFRepository) {
        this.cifRepo = cifRepo
    }

    override fun save(cifRes: CIFDetailedResult): CIFDetailedResult {
        return cifRepo.save(cifRes)
    }

    override fun delete(cifRes: CIFDetailedResult) {
        cifRepo.delete(cifRes)
    }

    override fun findAll(): Iterable<CIFDetailedResult> {
       return cifRepo.findAll()
    }

}