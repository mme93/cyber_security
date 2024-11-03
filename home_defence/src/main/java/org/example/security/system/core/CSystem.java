package org.example.security.system.core;

public class CSystem {

    public static class Tool {
        public static final String GIT = "git";
    }

   public static class Linux{
       public static class Command{
           public static final String VERSION="%s -v";

           //UFW
           public static final String UFW_ALL_PORTS="ufw status verbose";

       }
   }

}
