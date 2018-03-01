package com.wgsoftpro.DAO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.wgsoftpro.model.KursVO;

import javax.naming.NamingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by vkorshun on 27.11.2015.
 */
public class KursDAO {
  final static Logger LOGGER = LoggerFactory.getLogger(KursDAO.class);
  //private MikkoAdsConnection adsConnection;

  public KursDAO(){
      }

  public KursVO getKursVO(Integer kodv, Timestamp data) throws SQLException, NamingException {
    JdbcTemplate jdbcTemplate = new JdbcTemplate(BaseDAO.getDataSource());
    List<KursVO> result =jdbcTemplate.query("SELECT kodv,data,summa FROM kurs WHERE kodv=? and data=?",
        new Object[]{kodv, data}, new RowMapper<KursVO>() {
          @Override
          public KursVO mapRow(ResultSet resultSet, int i) throws SQLException {
            KursVO vo = new KursVO(resultSet.getInt("kodv"), resultSet.getTimestamp("data"),
                resultSet.getBigDecimal("summa"));
            return vo;
          }
        }
    );

    return result.size() == 0 ? new KursVO(kodv,data, null) : result.get(0);
  }

  public boolean setKusVO(KursVO kursVO ) {
    boolean retval = false;
    KursVO currentKursVO;
    try {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(BaseDAO.getDataSource());
      currentKursVO = getKursVO(kursVO.getKodv(), kursVO.getData());
      if (currentKursVO.getSumma() == null) {
        jdbcTemplate.update("INSERT INTO kurs(kodv,data,summa) VALUES(?,?,?) "
          , new Object[]{kursVO.getKodv(), kursVO.getData(), kursVO.getSumma()}
        );
      } else {
        jdbcTemplate.update("UPDATE kurs SET summa=? WHERE kodv=? AND data=? "
          , new Object[]{kursVO.getSumma(), kursVO.getKodv(), kursVO.getData()}
        );
      }
      retval = true;
    } catch (SQLException e) {
      LOGGER.error(e.getMessage());
    } catch (NamingException e) {
      LOGGER.error(e.getExplanation());
    }
    return retval;
  }

}
