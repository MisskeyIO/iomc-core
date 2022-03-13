package work.xeltica.craft.core.models;

import work.xeltica.craft.core.handlers.EbiPowerHandler;

/**
 * ヒントを定義しています。
 * @author Xeltica
 */
public enum Hint {
    // ようこそ
    WELCOME(
            "XelticaMCへようこそ！",
            "XelticaMCに初めてログインする",
            500
    ),
    GOTO_MAIN(
            "いざ居住区へ！",
            "メインワールドへ行く。メインワールドは建築・生活に適したワールド。採掘目的で地下を掘ってはいけない。",
            100
    ),
    GOTO_LOBBY(
            "ロビーへ戻る",
            "別世界からロビーに移動する",
            100
    ),
    GOTO_WILDAREA(
            "資源調達に行くぞ！",
            "ロビーからワイルドエリアAへ行く。ワイルドエリアAは普通のサバイバル・資源採取に自由に使えるワールド。",
            100
    ),
    GOTO_WILDAREAB(
            "広大な自然へ…",
            "X Phoneのテレポートアプリを用いて、メインワールドからワイルドエリアBに行く。ワイルドエリアBはAと異なり、月に1回再生成があるぞ。",
            100
    ),
    GOTO_WILDNETHER(
            "怖い地底世界",
            "ワイルドエリアAでネザーポータルを作り、行く。作り方は知ってるよね？",
            100
    ),
    GOTO_WILDEND(
            "はて？ここは果て？",
            "ワイルドエリアAでエンド要塞を見つけ、行く。要塞の意味は知ってるよね？",
            100
    ),
    GOTO_SANDBOX(
            "好きなだけ実験しよう",
            "サンドボックスへ行く。クリエイティブモードで好きなだけ実験できる。爆発物も思いのママ。あ、鯖は落とすなよ？",
            100
    ),
    GOTO_ART(
            "アーティストへの第一歩",
            "アートワールドへ行く。サンドボックスに近いが、こちらはクリエイティブ建築用ワールド。",
            100
    ),
    GOTO_NIGHTMARE(
            "悪い夢",
            "ナイトメアワールドへ行く。怖い敵がうじゃうじゃ、天候は最悪。だけど貰えるエビパワーは2倍！",
            100
    ),

    // 機能
    CAT_MODE(
            "にゃんか猫ににゃった",
            "ネコ語モードをオンにする。チャットの「な」が「にゃ」になる。",
            50
    ),
    BOAT(
            "ボートを出す",
            "/boat コマンドを使うか、X Phoneを用いてボートを出現させる",
            50
    ),
    MINECART(
            "トロッコを出す",
            "/cart コマンドを使うか、X Phoneを用いてトロッコを出現させる",
            50
    ),
    KUSA(
            "草",
            "草ブロックの上で「草」「www」などと発言して、ガチで草を生やす",
            50
    ),
    BE_CITIZEN(
            "晴れて市民になった！",
            "市民へ昇格する。詳しくは「市民システム」アプリを見てね。",
            500
    ),
    EPSHOP(
            "エビパワーストアデビュー",
            "エビパワーストアで商品を購入しよう。",
            200
    ),
    EPEFFECTSHOP(
            "困ったときのお薬屋さん",
            "エビパワードラッグストアで強力なポーション効果を購入しよう。",
            200
    ),
    TWIN_XPHONE(
            "そう、X Phoneならね。",
            "/phone コマンドを実行するとX Phoneが貰える。もし無くしたら試してみよう。",
            100
    ),
    COUNTDOWN(
            "カウントダウン",
            "/countdown コマンドは、仲間とかけっこしたり様々なことに使えるカウントダウンを表示できます。",
            100
    ),
    QUICKCHAT_APP(
            "クイックチャット",
            "X Phoneアプリ「クイックチャット」を使って簡単にメッセージを送ろう！",
            100
    ),
    QUICKCHAT(
            "点から始まるコミュニケーション",
            "X Phoneアプリ「クイックチャット」のメニューにかかれている英文字をチャットに打ち込もう。例: .k → こんにちは",
            100
    ),
    GET_BALL(
            "めざせモブマスター！",
            "モブボールを入手する。エビパワーストアで購入できる。",
            200
    ),
    FAILED_TO_CATCH_MOB(
            "いつもいつでもうまくゆくなんて保証はどこにもないけど",
            "体力が有り余っている、貴重である、強い、などの条件を持つモブは捕獲しづらいです。根気強くボールを投げましょう。",
            200
    ),
    SUCCEEDED_TO_CATCH_MOB(
            "モブ、ゲットだぜ！",
            "モブボールをモブに投げると捕獲することができます。捕まえる前に弱らせましょう。なお、自分のペットであれば100%捕獲できます。",
            500
    ),
    MINERS_DREAM(
            "マイナーズ・ドリーム",
            "ブロックをひたすら掘り続けて、1日で"
                    + EbiPowerHandler.BREAK_BLOCK_BONUS_LIMIT
                    + "EP稼ごう。石や鉱石、土などのブロックを採掘することで、1つ1EP、1日"
                    + EbiPowerHandler.BREAK_BLOCK_BONUS_LIMIT
                    + "EPまで稼ぐことができます。",
            2000,
            HintType.CHALLENGE
    ),

    // 暴力反対
    VIOLENCE_CHILD(
            "Don't touch the child!",
            "子どもモブを攻撃するとエビパワーが10減ります。殴っちゃダメよ。",
            10
    ),
    VIOLENCE_PET(
            "動物虐待、ダメ、ゼッタイ。",
            "誰かに飼いならされたモブを攻撃するとエビパワーが10減ります。殴っちゃダメよ。",
            10
    ),
    VIOLENCE_CAT(
            "猫を殴るなんて！！(-100)",
            "ネコちゃんを殴るなんて言語道断ですよね。エビパワーが100減ります。",
            100
    ),

    // おみくじ
    OMIKUJI_TOKUDAIKICHI(
            "運勢「特大吉」",
            "おみくじを引いて、特大吉を当てよう！0.001%の確率だ！特大吉になると、20分間「幸運」ポーション効果がつく上に、5%の確率で死亡時に「不死のトーテム」効果がつくぞ！",
            1000,
            HintType.CHALLENGE
    ),
    OMIKUJI_DAIKYOU(
            "運勢「大凶」",
            "おみくじを引いて、大凶を当てよう！0.001%の確率だ！大凶になると、「不運」「不吉の予感」「毒」効果がついてしまう…。",
            1000,
            HintType.CHALLENGE
    ),

    // ヘルプ
    EBIPOWER(
            "エビパワーとは",
            "エビパワーは、この世界で遊ぶほど貯まるポイントです。戦闘、採掘、ヒント達成、ログインボーナスなどで貰え、アイテム購入、飛行、おみくじなど様々なことに使えます。クラシックワールドでは貯まりません。"
    ),
    WEB(
            "公式サイト",
            "公式サイトには様々な情報が書かれています。規約も書いてあるので、一度読んでください。\n\nXelticaMCで検索。"
    ),
    WHAT_TO_DO(
            "やることがない",
            "やることに困ったら、まずはワイルドエリアAで資源集めをしましょう。その後、メインワールドに戻って、トロッコを使って電車に乗り、好きな場所にお家を建てましょう。"
    ),
    TRANSFER(
            "アカウント移行したい",
            "もしアカウントを新しく作って、エビパワーなどを新しいアカウントに引き継ぎたいなと思ったら、X Phoneの[引っ越し]アプリを使いましょう。"
    ),
    ;

    Hint(String name, String description) {
        this(name, description, 0);
    }

    Hint(String name, String description, int power) {
        this(name, description, power, HintType.NORMAL);
    }

    Hint(String name, String description, int power, HintType type) {
        this.name = name;
        this.description = description;
        this.power = power;
        this.type = type;
    }

    private final String name;
    private final String description;
    private final int power;
    private final HintType type;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public int getPower() {
        return this.power;
    }

    public HintType getType() {
        return this.type;
    }

    public enum HintType {
        NORMAL,
        CHALLENGE,
    }
}
