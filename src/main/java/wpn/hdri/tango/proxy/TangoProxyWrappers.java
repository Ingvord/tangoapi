package wpn.hdri.tango.proxy;

/**
 * @author Igor Khokhriakov <igor.khokhriakov@hzg.de>
 * @since 29.08.13
 */
public class TangoProxyWrappers {
    private TangoProxyWrappers() {
    }

    public static TangoProxyWrapper newInstance(String url) throws TangoProxyException {
        return new TangoProxyWrapperImpl(url);
    }
}
