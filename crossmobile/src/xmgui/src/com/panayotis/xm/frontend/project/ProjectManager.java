/* Copyright (c) 2011 by crossmobile.org
 *
 * CrossMobile is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 * CrossMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CrossMobile; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.panayotis.xm.frontend.project;

import com.panayotis.xm.frontend.XMPreferences;
import java.util.ArrayList;

public class ProjectManager {

    private static final int MAXHISTORY = 10;

    public static ArrayList<Project> getRecentProjects() {
        ArrayList<Project> projects = new ArrayList<Project>();
        ArrayList<String> list = getProjects();
        for (String path : list) {
            Project proj = new Project(path);
            if (proj.exists())
                projects.add(new Project(path));
            else
                removeProject(path);
        }
        return projects;
    }

    private static ArrayList<String> getProjects() {
        ArrayList<String> list = new ArrayList<String>(MAXHISTORY + 1); // +1 to accomodate new projects
        for (int i = 1; i <= MAXHISTORY; i++) {
            String item = XMPreferences.getProject(i);
            if (item != null)
                list.add(item);
        }
        return list;
    }

    private static void addProject(String newproject) {
        ArrayList<String> list = getProjects();
        int howmany = list.size();
        if (howmany >= MAXHISTORY) {
            list.add(0, newproject);
            for (int i = 0; i < MAXHISTORY; i++)
                XMPreferences.storeProject(i + 1, list.get(i));
        } else
            XMPreferences.storeProject(10 - howmany, newproject);
    }

    private static void removeProject(String brokenproject) {
        ArrayList<String> list = getProjects();
        int oldsize = list.size();
        list.remove(brokenproject);
        if (oldsize != list.size()) {
            int offset = 11 - list.size(); // if it has only one, the first will be #10
            for (int i = 0; i < list.size(); i++)
                XMPreferences.storeProject((i + offset), list.get(i));
            for (int i = list.size(); i < MAXHISTORY; i++)
                XMPreferences.removeProject(i + 1);
        }
    }
}
