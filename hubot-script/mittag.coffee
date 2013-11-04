# Configures the plugin
module.exports = (robot) ->
    # waits for the string "hubot mittag" to occur
    robot.respond /mittag/i, (msg) ->
        # Configures the url of a remote server
        msg.http('https://raw.github.com/offlinehoster/braas-kantinen-speiseplan/master/README.md')
            # and makes an http get call
            .get() (error, response, body) ->
                # passes back the complete reponse
                msg.send body
