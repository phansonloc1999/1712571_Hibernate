package Utils;

import java.util.List;

import com.example.SinhVien;

import org.hibernate.Query;
import org.hibernate.Session;

public class SinhVienDAO {
    public static List<SinhVien> getSinhVienList() {
        List<SinhVien> dsSinhVien = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        String hql = "from SinhVien";
        Query query = session.createQuery(hql);
        dsSinhVien = query.list();
        return dsSinhVien;
    }

}
