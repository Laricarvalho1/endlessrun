package ui;
import core.RankingService;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class TelaNomeJogador extends JDialog {

    private JTextField campoNome;

    public TelaNomeJogador(JFrame parent, int pontuacao) {
        super(parent, "Novo Recorde!", true);

        setTitle("Novo Recorde!");
        setSize(350, 220);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Título
        JLabel titulo = new JLabel("NOVO RECORDE!");
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(new Color(212, 175, 55)); // dourado

        // Subtítulo
        JLabel subtitulo = new JLabel("Digite seu nome para o ranking");
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));

        // Campo de nome
        campoNome = new JTextField();
        campoNome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        campoNome.setFont(new Font("Arial", Font.PLAIN, 14));
        campoNome.setHorizontalAlignment(JTextField.CENTER);

        // Botão
        JButton botaoSalvar = new JButton("Salvar");
        botaoSalvar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botaoSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoSalvar.setFocusPainted(false);


        botaoSalvar.addActionListener(e -> {
            String nome = campoNome.getText().trim();

            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Digite um nome!",
                        "Atenção",
                        JOptionPane.WARNING_MESSAGE
                );
            } else {
                RankingService.adicionarJogador(nome, pontuacao);
                dispose();
            }
        });

        painel.add(titulo);
        painel.add(Box.createVerticalStrut(5));
        painel.add(subtitulo);
        painel.add(Box.createVerticalStrut(15));
        painel.add(campoNome);
        painel.add(Box.createVerticalStrut(15));
        painel.add(botaoSalvar);

        add(painel);
    }
}
