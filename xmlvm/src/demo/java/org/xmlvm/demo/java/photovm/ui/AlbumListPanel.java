/*
 * Copyright (c) 2004-2009 XMLVM --- An XML-based Programming Language
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 * 
 * For more information, visit the XMLVM Home Page at http://www.xmlvm.org
 */

package org.xmlvm.demo.java.photovm.ui;

import org.xmlvm.demo.java.photovm.data.Album;
import org.xmlvm.demo.java.photovm.event.UpdateAlbumsListener;
import org.xmlvm.demo.java.photovm.event.UpdatePhotosListener;
import org.xmlvm.demo.java.photovm.net.PicasaRequest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * This panel shows a list of all albums available. When clicking on an entry, a
 * request is made and the listener will be given the resulting photos from that
 * request.
 * 
 * @author haeberling@google.com (Sascha Haeberling)
 */
public class AlbumListPanel extends ScrollPane implements UpdateAlbumsListener {
  final UpdatePhotosListener listener;

  public AlbumListPanel(final UpdatePhotosListener listener) {
    this.listener = listener;
    setBackground(new Color(30, 30, 30));
  }

  public void updateAlbums(List<Album> albums) {
    removeAll();
    Panel contentPanel = new Panel();
    contentPanel.setLayout(new GridLayout(0, 1));
    for (Album album : albums) {
      contentPanel.add(new AlbumEntry(album));
    }
    add(contentPanel);
    doLayout();
    validate();
  }

  /**
   * A single UI entry for an Album.
   * 
   * @author haeberling@google.com (Sascha Haeberling)
   */
  private class AlbumEntry extends Panel {
    public AlbumEntry(final Album album) {
      setLayout(new BorderLayout());
      Label lblTitle = new Label(maybeShortenTitle(album.getTitle()));
      lblTitle.setForeground(Color.WHITE);
      lblTitle.setFont(new Font("Arial", Font.PLAIN, 10));
      lblTitle.setAlignment(Label.CENTER);
      add(lblTitle, BorderLayout.NORTH);
      try {
        ImagePanel imagePanel = new ImagePanel(new URL(album.getCoverUrl()));
        imagePanel.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent arg0) {
            listener.updatePhotos(PicasaRequest.requestPhotos(album
                .getPhotoRequestUrl()));
          }
        });
        add(imagePanel, BorderLayout.CENTER);
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }

    private String maybeShortenTitle(String title) {
      if (title.length() <= 35) {
        return title;
      } else {
        return title.substring(0, 33) + "...";
      }
    }
  }
}
