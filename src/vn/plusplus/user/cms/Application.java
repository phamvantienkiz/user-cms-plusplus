package vn.plusplus.user.cms;

import vn.plusplus.user.cms.model.User;
import vn.plusplus.user.cms.service.UserService;

import java.util.List;
import java.util.Scanner;

public class Application {

    public static boolean login(String userName, String passWord)
    {
        UserService service = new UserService();
        List<User> users = service.readAllUserFromDB();
        for(int i=0;i<users.size();i++)
        {
            if(users.get(i).getUserName().equals(userName) && users.get(i).getPassword().equals(passWord))
            {
                    return true;
            }

        }
        return false;
    }

    public static void main(String[] args) throws Exception{
        boolean ketThuc = true;
        while (ketThuc){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Nhap lua chon su dung:");
            System.out.println("1. Dang ky\n" +
                    "2. Dang nhap\n" +
                    "3. Reset password\n" +
                    "4. Tim kiem\n" +
                    "5. Ket thuc");

            int option = scanner.nextInt();
            scanner.nextLine();
            UserService service = new UserService();
            switch (option){
                case 1:
                    System.out.println("Nhap username dang ky: ");
                    String userNameRegister = scanner.nextLine();
                    User userRe = service.findUserByUserName(userNameRegister);
                    if(userRe != null){
                        System.out.println("Ten dang nhap da ton tai, moi ban chon ten khac");
                    } else {
                        userRe = new User();
                        System.out.println("Nhap vao fullName:");
                        String fullName =scanner.nextLine();
                        service.saveUserToDB(userRe);
                    }
                    break;
                case 2:

                    System.out.println(" Nhập UserName: ");
                    String userName =scanner.nextLine();

                    System.out.println(" Nhập Password: ");
                    String passWord = scanner.nextLine();
                    if(login(userName,passWord) == true)
                    {
                        System.out.println(" Đăng nhập thành công!");
                    }
                    else
                    {
                        System.out.println(" Sai tên tài khoản hoặc mật khẩu!");
                    }
                    break;
                case 3:
                    System.out.println("Nhập Email đã đăng kí: ");
                    String email = scanner.nextLine();
                    User findUserEmai = service.getUserByEmail(email);
                    if (findUserEmai != null){
                        System.out.println("Để đảm bảo bạn là chủ sở hửu của tài khoản vui lòng nhập mật khẩu hiện tại");
                        String passwordNow = scanner.nextLine();
                        if (passwordNow.equals(findUserEmai.getPassword())){
                            service.sendTokenToEmail(email);
                            String usernames = findUserEmai.getUserName();
                            System.out.println("Kiểm tra Email và nhập mã đổi mật khẩu: ");
                            System.out.println("Nhập mã: ");
                            String token = scanner.nextLine();
                            if (service.checkToken(usernames, token) == true) {
                                System.out.println("Mời nhập mật khẩu mới: ");
                                String passwordNew = scanner.nextLine();
                                System.out.println("Nhập lại mật khẩu ở trên: ");
                                String passwordNew2 = scanner.nextLine();
                                if (passwordNew.equals(passwordNew2)){
                                    System.out.println("Đổi mật khẩu thành công !");
                                    findUserEmai.setPassword(passwordNew);
                                    service.saveUserToDB(findUserEmai);
                                    break;
                                } else {
                                    System.out.println("Mật khẩu không trùng nhau vui lòng nhập lại");
                                }
                            } else {
                                System.out.println("Mã không đúng kiểm tra và nhập lại");
                            }
                        } else {
                            System.out.println("Mật khẩu không đúng vui lòng nhập lại");
                        }
                    } else {
                        System.out.println("Không tìm thấy Email vui lòng nhập lại: ");
                    }

                case 5:
                    ketThuc = false;

            }
        }

    }

}
