package edu.school21.passbot.telegramview;

public class Buttons {
    public static final String SEPARATOR = " ";

    public static class Approve {
        public static final String TEXT = "Одобрить";
        public static final String CALLBACK = "approve";
    }

    public static class Decline {
        public static final String TEXT = "Отклонить";
        public static final String CALLBACK = "decline";
    }

    public static class Edit {
        public static final String TEXT = "Редактировать";
        public static final String CALLBACK = "edit";
    }

    public static class Delete {
        public static final String TEXT = "Удалить";
        public static final String CALLBACK = "delete ";
    }

}
