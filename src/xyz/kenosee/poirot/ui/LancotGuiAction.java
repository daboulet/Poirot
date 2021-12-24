package xyz.kenosee.poirot.ui;

import com.obtuse.exceptions.HowDidWeGetHereError;
import com.obtuse.ui.MyActionListener;
import com.obtuse.util.Logger;
import com.obtuse.util.ObtuseUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.Optional;

/**
 Implement a user action that affects an image.
 <p>Examples include image rotation and flipping.</p>
 */

public class LancotGuiAction<TARGET> extends AbstractAction {

    private final String _desc;
    private final int _mnemonic;
    private final MyActionListener _myActionListener;
    private final int _modifierMask;
    private final TARGET _target;

    protected LancotGuiAction(
            final @NotNull String name,
            final @Nullable ImageIcon icon,
            final @NotNull String desc,
            final int mnemonic,
            final int modifierMask,
            @NotNull TARGET target,
            @NotNull MyActionListener myActionListener

    ) {
        super();

        _desc = desc;
        _mnemonic = mnemonic;
        _modifierMask = modifierMask;

        _target = target;

        if ( icon != null ) {

            putValue( SMALL_ICON, icon );

        }

        putValue( NAME, name );
        putValue( SHORT_DESCRIPTION, desc );
        putValue( MNEMONIC_KEY, mnemonic );

        _myActionListener = myActionListener;

    }

    @NotNull
    public static JMenuItem createMenuItem(
            final LancotGuiAction<ImageAppearanceControlPanel.ImageAppearanceTarget> action,
            final JMenu jMenu
    ) {

        JMenuItem menuItem = new JMenuItem( action.getDesc() );

        menuItem.setIcon( null );
        menuItem.setAccelerator(
                keyStroke( action.getDesc(), action )
        );
        menuItem.setIcon( null );
        menuItem.addActionListener(
                action.getActionListener()
        );

        if ( jMenu != null ) {

            jMenu.add( menuItem );

        }

        return menuItem;

    }

    public static KeyStroke keyStroke(
            @SuppressWarnings("unused") @NotNull final String what,
            final LancotGuiAction<?> action
    ) {

        int mnemonic = action.getMnemonic();
        int menuShortcutKeyMaskEx = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        int modifierMask = action.getModifierMask();
        Optional<String> optName = action.getOptName();
        Logger.logMsg(
                action.getDesc() + "==" + ( optName.orElse( "missing" ) ) + " yields keyStroke( " +
                "'" + (char)mnemonic + "'" + " (" + (char)mnemonic + "=='" + (char)mnemonic + "')" +
                ", (" +
                ObtuseUtil.hexvalue( menuShortcutKeyMaskEx ) + "==" + menuShortcutKeyMaskEx +
                " | " +
                ObtuseUtil.hexvalue( modifierMask ) + "==" + modifierMask +
                ")==" + ( menuShortcutKeyMaskEx | modifierMask ) + " " +
                ")       modifiers=" + InputEvent.getModifiersExText( menuShortcutKeyMaskEx | modifierMask )
        );
        //noinspection MagicConstant
        return KeyStroke.getKeyStroke(
                mnemonic,
                menuShortcutKeyMaskEx | modifierMask
        );
    }

    public int getMnemonic() {

        return _mnemonic;

    }

    public int getModifierMask() {

        return _modifierMask;

    }

    public Optional<String> getOptName() {

        return Optional.ofNullable( (String)getValue( NAME ) );

    }

    @SuppressWarnings("unused")
    public String getMandatoryName() {

        Optional<String> optName = getOptName();
        if ( optName.isPresent() ) {

            return optName.get();

        } else {

            throw new HowDidWeGetHereError( "LancotGuiAction.getMandatoryName:  no name (we saved it earlier)" );


        }

    }

    public String getDesc() {

        return _desc;

    }

    @SuppressWarnings("unused")
    public TARGET getTarget() {

        return _target;

    }

    public void actionPerformed( ActionEvent e ) {

        _myActionListener.actionPerformed( e );

    }

    public MyActionListener getActionListener() {

        return _myActionListener;

    }

    public Optional<ImageIcon> getOptIcon() {

        return Optional.ofNullable( (ImageIcon)getValue( SMALL_ICON ) );

    }

}
