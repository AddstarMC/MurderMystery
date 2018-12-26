/*
 * Murder Mystery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Murder Mystery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Murder Mystery.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Murder Mystery is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Murder Mystery is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Murder Mystery.  If not, see <http://www.gnu.org/licenses/>.
 */

package pl.plajer.murdermystery.handlers.rewards;

import pl.plajerlair.core.rewards.Reward;

/**
 * @author Plajer
 * <p>
 * Created at 26.12.2018
 */
public class GameReward extends Reward {

  private RewardType type;

  public GameReward(RewardType type, String rawCode) {
    super(rawCode);
    this.type = type;
  }

  public RewardType getType() {
    return type;
  }

  public enum RewardType {
    DETECTIVE_KILL("detectivekill"), MURDERER_KILL("murdererkill"), END_GAME("endgame");

    private String path;

    RewardType(String path) {
      this.path = path;
    }

    public String getPath() {
      return path;
    }

  }

}