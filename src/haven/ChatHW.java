/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import ender.GoogleTranslator;

public class ChatHW extends HWindow {
    TextEntry in;
    Textlog out;
    static final Collection<Integer> todarken = new ArrayList<Integer>();
    
    static {
	Widget.addtype("slenchat", new WidgetFactory() {
		public Widget create(Coord c, Widget parent, Object[] args) {
		    String t = (String)args[0];
		    boolean cl = false;
		    if(args.length > 1)
			cl = (Integer)args[1] != 0;
		    return(new ChatHW(parent, t, cl));
		}
	    });
	todarken.add(Color.GREEN.getRGB());
	todarken.add(Color.CYAN.getRGB());
	todarken.add(Color.YELLOW.getRGB());
    }
	
    public ChatHW(Widget parent, String title, boolean closable) {
	super((Widget)ChatHWPanel.instance, title, closable);
	in = new TextEntry(new Coord(0, sz.y - 20), new Coord(sz.x, 20), this, "");
	in.canactivate = true;
	out = new Textlog(Coord.z, new Coord(sz.x, sz.y - 20), this);
	out.drawbg = false;
	if(cbtn != null) {
	    cbtn.raise();
	    if (title.equals("Area Chat"))
		cbtn.hide();
	}
	setsz(sz);
    }

    public void setsz(Coord s) {
	super.setsz(s);
	in.c = new Coord(0, sz.y - 20);
	in.sz = new Coord(sz.x, 20);
	out.sz = new Coord(sz.x, sz.y - 20);
    }
    
    public void uimsg(String msg, Object... args) {
	if(msg == "log") {
	    Color col = null;
	    if(args.length > 1)
		col = (Color)args[1];
	    if(args.length > 2)
		makeurgent((Integer)args[2]);
	    String str = (String)args[0];
	    if((col != null)&&(todarken.contains(col.getRGB())))
		col = col.darker();
	    str = GoogleTranslator.translate(str);
	    if(Config.timestamp)
		str = Utils.timestamp() + str;
	    out.append(str, col);
	} else if(msg == "focusme") {
	    shp.setawnd(this, true);
	    setfocus(in);
	} else {
	    super.uimsg(msg, args);
	}
    }
    
    public void wdgmsg(Widget sender, String msg, Object... args) {
	if(sender == in) {
	    if(msg == "activate") {
		wdgmsg("msg", args[0]);
		in.settext("");
		return;
	    }
	}
	super.wdgmsg(sender, msg, args);
    }
    public boolean mousewheel(Coord c, int amount)
    {
    	return(out.mousewheel(c, amount));
    }
}
