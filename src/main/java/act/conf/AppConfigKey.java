package act.conf;

import act.Act;
import act.app.App;
import act.handler.UnknownHttpMethodProcessor;
import act.view.TemplatePathResolver;
import act.view.View;
import org.osgl.$;
import org.osgl.exception.NotAppliedException;
import org.osgl.util.E;
import org.osgl.util.S;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * {@link App} configuration keys. General rules:
 * <p/>
 * <ul>
 * <li>When a key is ended with <code>.enabled</code>, then you should be able to set
 * the setting without <code>.enabled</code> or replace it with <code>.disabled</code>
 * but the value will be inverted. For example, <code>built_in.transformer.enabled</code>
 * is equal to <code>built_in.transformer</code> and invert to
 * <code>built_in.transformer.disabled</code></li>
 * <p/>
 * <li>When a key is ended with <code>.impl</code>, then you can either put an instance into
 * the configuration map or a string of the class className</li>
 * </ul>
 */
public enum AppConfigKey implements ConfigKey {

    /**
     * {@code act.basic_authentication.enabled} turn on/off Basic Authentication
     * in Act application.
     *
     * Default value: `false`
     *
     * **Note** there is no logic around this configuration in the core
     * ActFramework. It is up to the security plugins like `act-aaa-plugin`
     * to favor the value of this setting
     */
    BASIC_AUTHENTICATION("basic_authentication.enabled"),

    /**
     * {@code act.cors.enabled} turn on/off CORS in Act application
     *
     * Default value: `false`
     */
    CORS("cors.enabled"),

    /**
     * `act.cors.option.check` specify whether the framework should
     * check the current request is an HTTP OPTION method before applying
     * controller headers or not
     *
     * default value: `true`
     */
    CORS_CHECK_OPTION_METHOD("cors.option.check.enabled"),

    /**
     * {@code act.cors.origin} specifies `Access-Control-Allow-Origin` header
     * to be output
     *
     * Default value: `*`
     */
    CORS_ORIGIN("cors.origin"),

    /**
     * {@code act.cors.headers} specifies both `Access-Control-Expose-Headers`
     * and `Access-Control-Allow-Headers`
     *
     * Default value: `Content-Type, X-HTTP-Method-Override`
     */
    CORS_HEADERS("cors.headers"),

    /**
     * {@code act.cors.headers.expose} specify `Access-Control-Expose-Headers`.
     * Note this setting will overwrite the setting of {@link #CORS_HEADERS} if
     * it is set
     *
     * Default value: empty
     */
    CORS_HEADERS_EXPOSE("cors.headers.expose"),

    /**
     * {@code act.cors.headers.expose} specify `Access-Control-Allow-Headers`.
     * Note this setting will overwrite the setting of {@link #CORS_HEADERS} if
     * it is set
     *
     * Default value: empty
     */
    CORS_HEADERS_ALLOWED("cors.headers.allowed"),

    /**
     * {@code act.cors.max_age} specifies `Access-Control-Max-Age`.
     *
     * Default value: 30*60 (seconds)
     */
    CORS_MAX_AGE("cors.max_age"),

    /**
     * {@code act.csrf.enabled} turn on/off global CSRF protect
     *
     * Default value: `true`
     */
    CSRF("csrf.enabled"),

    /**
     * {@code act.csf.param_name} specifies the http request param name
     * used to convey the csrf token
     *
     * Default value: the value of {@link act.app.ActionContext#ATTR_CSRF_TOKEN}
     */
    CSRF_PARAM_NAME("csf.param_name"),

    /**
     * {@code act.csrf.header_name} specifies name of the http request
     * header used to convey the csrf token sent from AJAX client.
     *
     * Default value: `X-Xsrf-Token` - the name used by AngularJs
     */
    CSRF_HEADER_NAME("csrf.header_name"),

    /**
     * {@code act.csrf.cookie_name} specify the name of the cookie used
     * to convey the csrf token generated on the server for the first GET
     * request coming from a client.
     *
     * Default value: `XSRF-TOKEN` - the name used by AngularJs
     */
    CSRF_COOKIE_NAME("csrf.cookie_name"),

    /**
     * `act.csrf.protector` specifies the implementation of
     * {@link act.security.CSRFProtector}.
     *
     * The value of this configuration could be either a name of
     * the class that implements {@link act.security.CSRFProtector}
     * interface, or the enum name of {@link act.security.CSRFProtector.Predefined}
     *
     * Default value: `HMAC` which specifies the {@link act.security.CSRFProtector.Predefined#HMAC}
     */
    CSRF_PROTECTOR("csrf.protector"),

    /**
     * {@code act.cli.port} specifies the default cli (telnet) port the application
     * listen to.
     * <p>Default value: {@code 5461}</p>
     */
    CLI_PORT("cli.port"),

    /**
     * `act.cli_over_http.enabled` turn on/off CLI over http feature, which
     * allows ActFramework to handle http request sent through to the  {@link #CLI_OVER_HTTP_PORT}
     * as a way to invoke CLI commands and inspect results
     *
     * Default value: `false`
     */
    CLI_OVER_HTTP("cli_over_http.enabled"),

    /**
     * `act.cli_over_http.authority` specifies the {@link act.cli.CliOverHttpAuthority} implementation
     */
    CLI_OVER_HTTP_AUTHORITY("cli_over_http.authority.impl"),

    /**
     * `act.cli_over_http.port` specifies the default cli over http port the application
     * listen to.
     *
     * Default value: `5462`
     */
    CLI_OVER_HTTP_PORT("cli_over_http.port"),

    /**
     * `act.cli_over_http.port` specify the title to be displayed on the CLI Over Http
     * page
     *
     * Default value: "Cli Over Http"
     */
    CLI_OVER_HTTP_TITLE("cli_over_http.title"),

    /**
     * `act.cli_over_http.syscmd.enabled` turn on/off system command on CLI Over Http
     * page
     *
     * Default value: `true`
     */
    CLI_OVER_HTTP_SYS_CMD("cli_over_http.syscmd.enabled"),

    /**
     * {@code cli.session.expiration} specifies the number of seconds
     * a cli session can exists after last user interaction
     * <p>Default value: {@code 300} seconds. e.g. 5 minutes</p>
     */
    CLI_SESSION_EXPIRATION("cli.session.expiration"),

    /**
     * {@code cli.session.max} specifies the maximum number of cli threads
     * can exists concurrently
     * <p>Default value: {@code 3}</p>
     */
    CLI_SESSION_MAX("cli.session.max"),

    /**
     * {@code act.configure.impl}
     * <p>Specifies the application configuration class which provides default configuration
     * via source code. The settings provided by application configuration class might be
     * overwritten by configuration file</p>
     * <p>Default value: {@code null}</p>
     */
    CONFIG_IMPL("config.impl"),

    /**
     * {@code act.cache.impl}
     * Specify {@link org.osgl.cache.CacheServiceProvider Cache service provider}
     * <p>Default value: {@link org.osgl.cache.CacheServiceProvider.Impl#Simple the simple
     * in memory map based cache service implementation}</p>
     */
    CACHE_IMPL("cache.impl"),

    /**
     * {@code act.cli.table.page.size}
     * Specify the maximum records in one page for table layout by CLI command
     *
     * Default value: 22
     */
    CLI_TABLE_PAGE_SIZE("cli.table.page.size"),

    /**
     * {@code act.cli.json.page.size}
     * Specify the maximum records in one page for JSON layout by CLI command
     *
     * Default value: 10
     */
    CLI_JSON_PAGE_SIZE("cli.json.page.size"),

    /**
     * {@code act.content_suffix.aware.enabled}
     * <p>
     *     Once enabled then the framework automatically recognize request with content suffix.
     *     E.g. {@code /customer/123/json} will match the route {@code /customer/123} and set the
     *     request {@code Accept} header to {@code text/xml}
     * </p>
     * <p>Default value: {@code false}</p>
     */
    CONTENT_SUFFIX_AWARE("content_suffix.aware.enabled"),

    /**
     * {@code act.controller_package} specify the java
     * package where controller classes are aggregated.
     * <p>Once controller_package is specified then the application developer could
     * write short request handler in the routing table. For example, suppose an original
     * routing table is specified as</p>
     * <table>
     * <tr>
     * <th>HTTP Method</th>
     * <th>URL Path</th>
     * <th>Action handler</th>
     * </tr>
     * <tr>
     * <td>GET</td>
     * <td>/users</td>
     * <td>com.mycorp.myproj.controllers.UserController.list</td>
     * </tr>
     * </table>
     * <p>If {@code act.controller_package} is specified as {@code com.mycorp.myproj.controllers}
     * then the routing table could be simplified as:</p>
     * <table>
     * <tr>
     * <th>HTTP Method</th>
     * <th>URL Path</th>
     * <th>Action handler</th>
     * </tr>
     * <tr>
     * <td>GET</td>
     * <td>/users</td>
     * <td>UserController.list</td>
     * </tr>
     * </table>
     */
    @Deprecated
    CONTROLLER_PACKAGE("controller_package"),

    /**
     * `act.cookie.domain_provider` specify the provider
     * that provides the cookie domain name
     *
     * Default value: value of {@link #HOST}
     */
    COOKIE_DOMAIN_PROVIDER("cookie.domain_provider"),

    /**
     * `act.db.seq_gen.impl` specifies the implementation of
     * {@link act.db.util._SequenceNumberGenerator}.
     *
     * Default value: `null`
     */
    DB_SEQ_GENERATOR("db.seq_gen.impl"),

    /**
     * {@code act.encoding} specifies application default encoding
     * <p>Default value: utf-8</p>
     */
    ENCODING("encoding"),

    /**
     * {@code act.fmt.date} specifies the default date format used to
     * lookup/output the date string
     * <p>Default value: the pattern of {@code java.text.DateFormat.getDateInstance()}</p>
     */
    FORMAT_DATE("fmt.date"),


    /**
     * {@code act.fmt.date} specifies the default date and time format used to
     * lookup/output the date string
     * <p>Default value: the pattern of {@code java.text.DateFormat.getDateTimeInstance()}</p>
     */
    FORMAT_DATE_TIME("fmt.date_time"),

    /**
     * {@code act.fmt.time} specifies the default time format used to
     * lookup/output the date time string
     * <p>Default value: the pattern of {@code java.text.DateFormat.getTimeInstance()}</p>
     */
    FORMAT_TIME("fmt.time"),

    /**
     * {@code act.host} specifies the host the application
     * reside on.
     * <p/>
     * <p>Default value: {@code localhost}</p>
     */
    HOST("host"),

    /**
     * `act.http.external_server.enabled` specify if the app is running behind a front end
     * http server
     *
     * Default value: `true` when running in PROD mode; `false` when running in DEV mode
     */
    HTTP_EXTERNAL_SERVER("http.external_server.enabled"),

    /**
     * `act.http.port.external` set the external port which is used to
     * construct the full url.
     *
     * Note act does not listen to external port directly. The recommended
     * pattern is to have a front end HTTP server (e.g. nginx) to handle
     * the external request and forward to act
     *
     * Default value: `80`
     */
    HTTP_EXTERNAL_PORT("http.port.external"),

    /**
     * `act.http.port.external.secure` set the external secure port which is
     * used to construct full url string when app is running secure mode
     *
     * @see #HTTP_EXTERNAL_PORT
     */
    HTTP_EXTERNAL_SECURE_PORT("http.port.external.secure"),

    /**
     * {@code act.http.params.max} specifies the maximum number of http parameters
     * this is to prevent the hash collision DOS attack
     * <p>Default value: {@code 1000}</p>
     */
    HTTP_MAX_PARAMS("http.params.max"),

    /**
     * {@code act.http.port} specifies the default http port the application
     * listen to. This is preferred way to dispatch the http request to the
     * application.
     * <p/>
     * <p>Default value: {@code 5460}</p>
     */
    HTTP_PORT("http.port"),

    /**
     * {@code act.http.secure} specifies whether the default http port is
     * running https or http.
     * <p></p>
     * <p>
     *     Default value: {@code false} when Act is running in dev mode
     *     or {@code true} when Act is running in prod mode
     * </p>
     */
    @SuppressWarnings("unchecked")
    HTTP_SECURE("http.secure.enabled"),

    /**
     * `act.i18n.enabled` turn on/off i18n tools, e.g. {@link act.i18n.LocaleResolver}
     *
     * Default value: `false`
     */
    I18N("i18n.enabled"),

    /**
     * `act.i18n.locale.param_name` specifies the param name to set client locale in http request
     *
     * Default value: `act_locale`
     */
    I18N_LOCALE_PARAM_NAME("i18n.locale.param_name"),

    /**
     * `act.i18n.locale.cookie_name` specifies the name for the locale cookie
     *
     * Default value: `act_locale`
     */
    I18N_LOCALE_COOKIE_NAME("i18n.locale.cookie_name"),

    /**
     * {@code act.idgen.node_id.provider.impl} specifies the {@link act.util.IdGenerator.NodeIdProvider}
     * implementation for {@link App#idGenerator}
     * <p>Default value: {@link act.util.IdGenerator.NodeIdProvider.IpProvider}</p>
     */
    ID_GEN_NODE_ID_PROVIDER("idgen.node_id.provider.impl"),

    /**
     * {@code act.idgen.node_id.effective_ip_bytes} specifies how many bytes in the ip address
     * will be used to calculate node ID. Usually in a cluster environment, the ip address will
     * be different at only (last) one byte or (last) two bytes, in which case it could set this
     * configuration to {@code 1} or {@code 2}. When the configuration is set to {@code 4} then
     * it means all 4 IP bytes will be used to calculate the node ID
     * <p>Default value: {@code 4}</p>
     */
    ID_GEN_NODE_ID_EFFECTIVE_IP_BYTES("idgen.node_id.effective_ip_bytes.size"),

    /**
     * {@code act.idgen.start_id.provider.impl} specifies the {@link act.util.IdGenerator.StartIdProvider}
     * implementation for {@link App#idGenerator}
     * <p>Default value: {@link act.util.IdGenerator.StartIdProvider.DefaultStartIdProvider}</p>
     */
    ID_GEN_START_ID_PROVIDER("idgen.start_id.provider.impl"),

    /**
     * {@code act.idgen.start_id.file} specifies the start id persistent file for
     * {@link act.util.IdGenerator.StartIdProvider.FileBasedStartCounter}
     * <p>Default value: {@code act_start.id}</p>
     */
    ID_GEN_START_ID_FILE("idgen.start_id.file"),

    /**
     * {@code act.idgen.seq_id.provider.impl} specifies the {@link act.util.IdGenerator.SequenceProvider}
     * implementation for {@link App#idGenerator}
     * <p>Default value: {@link act.util.IdGenerator.SequenceProvider.AtomicLongSeq}</p>
     */
    ID_GEN_SEQ_ID_PROVIDER("idgen.seq_id.provider.impl"),

    /**
     * {@code act.idgen.encoder.impl} specifies the {@link act.util.IdGenerator.LongEncoder}
     * implementation for {@link App#idGenerator}
     * <p>Default value: {@link act.util.IdGenerator.SafeLongEncoder}</p>
     */
    ID_GEN_LONG_ENCODER("idgen.encoder.impl"),

    /**
     * {@code act.locale} specifies the application default locale
     * <p>Default value: {@link java.util.Locale#getDefault}</p>
     */
    LOCALE("locale") {
        @Override
        public <T> T val(Map<String, ?> configuration) {
            Object o = super.val(configuration);
            if (null == o) {
                return null;
            }
            if (o instanceof String) {
                return (T) Locale.forLanguageTag((String) o);
            } else if (o instanceof Locale) {
                return (T) o;
            } else {
                String s = o.toString();
                return (T) Locale.forLanguageTag(s);
            }
        }
    },

    /**
     * {@code job.pool.size} specifies the maximum number of threads
     * can exists in the application's job manager's thread pool
     * <p>Default value: {@code 10}</p>
     */
    JOB_POOL_SIZE("job.pool.siz"),

    /**
     * {@code act.modules}
     *
     * Declare additional app base (for maven modules)
     *
     * Default value: `null`
     */
    MODULES("modules"),

    /**
     * {@code act.namedPorts} specifies a list of port names this
     * application listen to. These are additional ports other than
     * the default {@link #HTTP_PORT}
     * <p/>
     * The list is specified as
     * <pre><code>
     * act.namedPorts=admin:8888;ipc:8899
     * </code></pre>
     * <p>Default value: {@code null}</p>
     * <p>Note, the default port that specified in {@link #HTTP_PORT} configuration
     * and shall not be specified in this namedPorts configuration</p>
     */
    NAMED_PORTS("namedPorts"),

    /**
     * {@code ping.path} specify the ping path.
     * If this setting is specified, then when session resolving, system
     * will check if the current URL matches the setting. If matched
     * then session cookie expiration time will not be changed. Otherwise
     * the expiration time will refresh
     * <p>Default value: {@code null}</p>
     */
    PING_PATH("ping.path"),

    /**
     * {@code profile} specifies the profile to load configuration
     * If this setting is specified, and there is a folder named as
     * the {@code profile} setting sit under {@code /resource/conf}
     * folder, then the properties files will be loaded from
     * that folder.
     * <p>Default value: the value of the {@link Act#mode()}</p>
     * <p>Note, unlike other configuration items which is usually specified
     * in the configuration file. {@code profile} setting is load
     * by {@link System#getProperty(String)}</p>, thus it is usually
     * specified with JVM argument {@code -Dprofile=<profile>}
     */
    PROFILE("profile"),


    /**
     * {@code resolver.error_template_path.impl} specifies error page (template)
     * path resolver implementation
     * <p>Default value: {@code act.util.ErrorTemplatePathResolver.DefaultErrorTemplatePathResolver}</p>
     */
    RESOLVER_ERROR_TEMPLATE_PATH("resolver.error_template_path.impl"),


    /**
     * {@code resolver.template_path.impl} specifies the class that
     * implements {@link TemplatePathResolver}. Application
     * developer could use this configuration to add some flexibility to
     * template path resolving logic, e.g. different home for different locale
     * or different home for different device type etc.
     * <p/>
     * <p>Default value: {@link TemplatePathResolver}</p>
     */
    RESOLVER_TEMPLATE_PATH("resolver.template_path.impl"),

    /**
     * {@code scan_package}
     * Specify the app package in which all classes is subject
     * to bytecode processing, e.g enhancement and injection.
     * This setting should be specified when application loaded.
     * Otherwise Act will try to process all classes found in
     * application's lib and classes folder, which might cause
     * performance issue on loading
     */
    SCAN_PACKAGE("scan_package"),

    /**
     * {@code secret}
     * Specifies the secret key the application used to do general
     * encrypt/decrypt/sign etc
     * <p>Default value: {@code myawesomeapp}</p>
     */
    SECRET("secret"),

    /**
     * {@code session.prefix} specifies the prefix to be prepended
     * to the session cookie name. Let's say the default cookie name is
     * {@code act_session}, and user specifies the prefix {@code my_app}
     * then the session cookie name will be {@code my_app_session}.
     * <p>Note this setting also impact the {@link AppConfig#flashCookieName()}</p>
     * <p>Default value: {@code act}</p>
     */
    SESSION_PREFIX("session.prefix"),

    /**
     * {@code session.ttl} specifies the session duration in seconds.
     * If user failed to interact with server for amount of time that
     * exceeds the setting then the session will be destroyed
     *
     * <p>Default value: {@code 60 * 30} i.e half an hour</p>
     */
    SESSION_TTL("session.ttl"),

    /**
     * {@code session.persistent.enabled} specify whether the system
     * should treat session cookie as persistent cookie. If this setting
     * is enabled, then the user's session will not be destroyed after
     * browser closed.
     *
     * <p>Default value: {@code false}</p>
     *
     * See <a href="http://en.wikipedia.org/wiki/HTTP_cookie#Persistent_cookie">HTTP_cookie</a>
     */
    SESSION_PERSISTENT_ENABLED("session.persistent.enabled"),

    /**
     * {@code session.encrypted.enabled} specify whether the system should
     * encrypt the key/value pairs in the session cookie. Enable session
     * encryption will greatly improve the security but with the cost
     * of additional CPU usage and a little bit longer time on request
     * processing.
     *
     * <p>Default value: {@code false}</p>
     */
    SESSION_ENCRYPT_ENABLED("session.encrypt.enabled"),

    /**
     * `act.session.key.username` specifies the session key for username
     *
     * Default value: `username`
     */
    SESSION_KEY_USERNAME("session.key.username"),

    /**
     * {@code session.mapper.impl} specify the implementation of {@link act.util.SessionMapper}
     *
     * <p>Default value: {@code act.util.SessionMapper.DefaultSessionMapper}</p>
     */
    SESSION_MAPPER("session.mapper.impl"),

    /**
     * {@code session.secure.enabled} specifies whether the session cookie should
     * be set as secure. Enable secure session will cause session cookie only
     * effective in https connection. Literally this will enforce the web site to run
     * default by https.
     *
     * <p>Default value: {@code true}</p>
     *
     * <p><b>Note</b> when {@link Act Act server} is running in {@link Act.Mode#DEV mode}
     * session http only will be disabled without regarding to the {@code session.secure.enabled}
     * setting</p>
     */
    SESSION_SECURE("session.secure.enabled"),

    /**
     * {@code source_version} specifies the java version
     * of the src code. This configuration is used only
     * in dev mode.
     * <p>Default value: 1.7</p>
     */
    SOURCE_VERSION("source_version"),

    /**
     * {@code act.source_version} specifies the java version
     * of the compile target code. This configuration is used only
     * in dev mode.
     * <p>Default value: 1.7</p>
     */
    TARGET_VERSION("target_version"),

    /**
     * {@code template.home} specifies where the view templates resides.
     * If not specified then will use the {@link View#name() view name
     * in lower case} as the template home if that view is used.
     * <p/>
     * <p>Default value: {@code default}</p>
     */
    TEMPLATE_HOME("template.home"),

    /**
     * {@code unknown_http_method_handler} specifies a class/instance that
     * implements {@link UnknownHttpMethodProcessor} that process
     * the HTTP methods that are not recognized by {@link act.route.Router},
     * e.g. "OPTION", "PATCH" etc
     */
    UNKNOWN_HTTP_METHOD_HANDLER("unknown_http_method_handler.impl"),

    /**
     * {@code url_context} specifies the context part
     * of the URL. This is used for Act to dispatch the
     * incoming request to the application. Usually
     * the {@link #HTTP_PORT port} configuration is preferred
     * than this configuration
     * <p/>
     * <p>Default value is empty string</p>
     */
    URL_CONTEXT("url_context"),

    /**
     * {@code validation.message.interpolator.impl} specifies the
     * {@link javax.validation.MessageInterpolator} implementation
     * <p>Default value: {@link act.validation.ValidationMessageInterpolator}</p>
     */
    VALIDATION_MSG_INTERPOLATOR("validation.message.interpolator.impl"),

    /**
     * {@code url.login} specifies the login URL which is used
     * by {@link act.util.RedirectToLoginUrl}
     * <p>Default value: {@code /login}</p>
     */
    LOGIN_URL("url.login"),

    /**
     * {@code url.login.ajax} specifies the login URL which is used
     * by {@link act.util.RedirectToLoginUrl} when request is AJAX
     * <p>Default value: the value of {@link #LOGIN_URL}</p>
     */
    AJAX_LOGIN_URL("url.login.ajax"),

    /**
     * {@code handler.missing_authentication.impl} specifies the implementation
     * for {@link act.util.MissingAuthenticationHandler}
     * <p>Default value: {@link act.util.RedirectToLoginUrl}</p>
     */
    MISSING_AUTHENTICATION_HANDLER("handler.missing_authentication.impl"),

    /**
     * `act.handler.csrf_check_failure.impl` specifies the implementation
     * for {@link act.util.MissingAuthenticationHandler}
     *
     * Default value: {@link act.util.RedirectToLoginUrl}
     */
    CSRF_CHECK_FAILURE_HANDLER("handler.csrf_check_failure.impl"),

    /**
     * {@code handler.missing_authentication.ajax.impl} specifies the implementation
     * for {@link act.util.MissingAuthenticationHandler} dealing with the case of AJAX
     * request
     * <p>Default value: the value of {@link #MISSING_AUTHENTICATION_HANDLER}</p>
     */
    AJAX_MISSING_AUTHENTICATION_HANDLER("handler.missing_authentication.ajax.impl"),

    /**
     * `act.handler.csrf_check_failure.ajax.impl` specifies the implementation for
     * {@link act.util.MissingAuthenticationHandler} dealing with the case of AJAX
     * request
     *
     * Default value: the value of {@link #CSRF_CHECK_FAILURE_HANDLER}
     */
    AJAX_CSRF_CHECK_FAILURE_HANDLER("handler.csrf_check_failure.ajax.impl"),

    /**
     * {@code act.view.default} specifies the default view solution. If there
     * are multiple views registered and default view are available, then
     * it will be used at priority to load the templates
     * <p/>
     * <p>Default value: {@code rythm}</p>
     */
    VIEW_DEFAULT("view.default"),

    X_FORWARD_PROTOCOL("x_forward_protocol"),

    ;
    private String key;
    private Object defVal;
    static ConfigKeyHelper helper = new ConfigKeyHelper(Act.F.MODE_ACCESSOR);

    AppConfigKey(String key) {
        this(key, null);
    }

    AppConfigKey(String key, Object defVal) {
        this.key = key;
        this.defVal = defVal;
    }

    public static void onApp(final App app) {
        helper.classLoaderProvider(new $.F0<ClassLoader>() {
            @Override
            public ClassLoader apply() throws NotAppliedException, $.Break {
                return app.classLoader();
            }
        });
    }

    /**
     * Return the key string
     *
     * @return the key of the configuration
     */
    public String key() {
        return key;
    }

    /**
     * Return default value of this setting. The configuration data map
     * is passed in in case the default value be variable depending on
     * another setting.
     *
     * @param configuration
     * @return return the default value
     */
    protected Object getDefVal(Map<String, ?> configuration) {
        return defVal;
    }

    /**
     * Calling to this method is equals to calling {@link #key()}
     *
     * @return key of the configuration
     */
    @Override
    public String toString() {
        return key;
    }

    @Override
    public Object defVal() {
        return defVal;
    }

    public <T> List<T> implList(String key, Map<String, ?> configuration, Class<T> c) {
        return helper.getImplList(key, configuration, c);
    }

    /**
     * Return configuration value from the configuration data map using the {@link #key}
     * of this {@link AppConfigKey setting} instance
     *
     * @param configuration
     * @param <T>
     * @return return the configuration
     */
    public <T> T val(Map<String, ?> configuration) {
        return helper.getConfiguration(this, configuration);
    }

    private static Map<String, AppConfigKey> lookup = new HashMap<String, AppConfigKey>(50);

    static {
        for (AppConfigKey k : values()) {
            lookup.put(k.key().toLowerCase(), k);
        }
    }

    /**
     * Return key enum instance from the string in case insensitive mode
     *
     * @param s
     * @return configuration key from the string
     */
    public static AppConfigKey valueOfIgnoreCase(String s) {
        E.illegalArgumentIf(S.blank(s), "config key cannot be empty");
        return lookup.get(s.trim().toLowerCase());
    }

}
